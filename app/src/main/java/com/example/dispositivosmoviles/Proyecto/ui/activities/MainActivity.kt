package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PermissionResult
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.Proyecto.data.validator.LoginValidator
import com.example.dispositivosmoviles.Proyecto.ui.utilities.MyLocationManager
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.Permission
import java.util.Locale
import java.util.UUID

//parte del DATA STORE
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")// es una percostencia temporal
//---------------------

class MainActivity : AppCompatActivity() {

    //variable de firebase
    private lateinit var auth: FirebaseAuth
//    private val TAG = "UCE"

    private lateinit var binding: ActivityMainBinding
    // (Lateinit) un gran poder conlleva una gran responsabilidad

    //---------- localicacion del usuario
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //-------------------------------
//UBICACION Y GPS
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallbak: LocationCallback
    private var currentLocation: Location? = null
    private lateinit var client: SettingsClient
    private lateinit var locationSettingRequest: LocationSettingsRequest

    private val speechToText =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val sn = Snackbar.make(
                binding.textView3,
                "",
                Snackbar.LENGTH_LONG
            )
            var message = ""
            when (activityResult.resultCode) {
                RESULT_OK -> {
                    val msg =
                        activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                            ?.get(0)
                            .toString()
                    if (msg.isNotEmpty()) {
                        val intent = Intent(
                            Intent.ACTION_WEB_SEARCH
                        )
                        //Los parametros para abrir una aplicacion especifica
                        intent.setClassName(
                            "com.google.android.googlequicksearchbox",
                            "com.google.android.googlequicksearchbox.SearchActivity"
                        )
                        intent.putExtra(
                            SearchManager.QUERY,
                            msg
                        )//es un query que me permite buscar algo determinado
                        startActivity(intent)
                    }
                }

                RESULT_CANCELED -> {
                    message = "proceso cancelado"
                    sn.setBackgroundTint(resources.getColor(R.color.rojo_pasion))
                }

                else -> {
                    message = "ocurrio un error"
                    sn.setBackgroundTint(resources.getColor(R.color.rojo_pasion))
                }
            }
            sn.setText(message)
            sn.show()


        }

    @SuppressLint("MissingPermission")
    private val locationContract =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    client.checkLocationSettings(locationSettingRequest).apply {
                        addOnSuccessListener {
                            val task = fusedLocationProviderClient.lastLocation
                            task.addOnSuccessListener { location ->
                                fusedLocationProviderClient.requestLocationUpdates(
                                    locationRequest,
                                    locationCallbak,
                                    Looper.getMainLooper()
                                )
                            }

                        }
                        addOnFailureListener { ex ->
                            if (ex is ResolvableApiException) {
                                ex.startResolutionForResult(
                                    this@MainActivity,
                                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                                )
//                                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                            }

                        }
                    }


                    //  val task = fusedLocationProviderClient.lastLocation
//                    task.addOnSuccessListener { location ->
//                        val alert=AlertDialog.Builder(this)
//                        alert.apply {
//                            setTitle("alerta")
//                            setMessage("Existe un problema de posicionamiento global en el sistema")
//                            setPositiveButton("ok"){ dialog,id->
//                                dialog.dismiss()
//                            }
//                            setCancelable(false)
//                        }.create()
//                        alert.show()
//                        fusedLocationProviderClient.requestLocationUpdates(
//                            locationRequest,
//                            locationCallbak,
//                            Looper.getMainLooper()
//                        )
//                    }


                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

                }

                false -> {

                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //-------------------------variable de location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        ).build()

        locationCallbak = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult != null) {
                    locationResult.locations.forEach { location ->
                        currentLocation = location
                        Log.d("UCE", "Ubicacion:${location.latitude}," + "${location.longitude}")
                    }
                }
            }
        }

        client = LocationServices.getSettingsClient(this)
        locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()


        //---------------------------firebase
        auth = Firebase.auth
//        binding.btnLogin.setOnClickListener {
//            signInWithEmailAndPassword(
//                binding.txtUser.text.toString(),
//                binding.txtPassword.text.toString()
//            )
//        }
        binding.btnLogin.setOnClickListener {
            authWithFirebaseEmail(
                binding.txtUser.text.toString(),
                binding.txtPassword.text.toString()
            )
        }

    }

    private fun authWithFirebaseEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this,BiometricActivity::class.java))

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    private fun recoveryPasswordWitnEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Correo de recuperacion enviado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Alerta")
                        setMessage("Su correo de recuperacion ha sido procesado correcto")
                        setCancelable(true)
                    }
                }

            }
    }

    override fun onStart() {
        super.onStart()
//        initClass()
        //val db = DispositivosMoviles.getDbInstance()
        //db.marvelDao()

    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallbak)
    }

    @SuppressLint("MissingPermission")
    private fun initClass() {
        binding.btnLogin.setOnClickListener {
            val username = binding.txtUser.text.toString()
            val password = binding.txtPassword.text.toString()

            val isValid = LoginValidator().checkLogin(username, password)

            if (isValid) {

                //para utilizar data store
                //se utiliza una corrutina para utilizar este metodo savedataStore y se guarda los datos por eso se utiliza IO
                lifecycleScope.launch(Dispatchers.IO) {
                    saveDataStore(binding.txtUser.text.toString())
                }
                //------------------------------------

                val intent = Intent(this, PrincipalActivity::class.java)
                intent.putExtra("var1", username)
                startActivity(intent)


            } else {
                Snackbar.make(
                    binding.textView2,
                    "Usuario o contraseña inválidos",
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.BLACK).show()
            }
        }

        //-----------------------------------------------permiso
        //lanzado de permisos necesitamos un RequestPermission()
//        val locationContract =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                when (isGranted) {
//                    true -> {
//                        val task = fusedLocationProviderClient.lastLocation
//
//                        task.addOnSuccessListener {
//                            if (task.result != null) {
//                                Snackbar.make(
//                                    binding.textView3,
//                                    "${it.latitude},${it.longitude}",
//                                    Snackbar.LENGTH_LONG
//                                )
//                                    .show()
//                            } else {
//                                Snackbar.make(
//                                    binding.textView3,
//                                    "encienda el gps porfavor",
//                                    Snackbar.LENGTH_LONG
//                                )
//                                    .show()
//                            }
//                        }
////                    Snackbar.make(binding.textView3, "permiso concedido", Snackbar.LENGTH_LONG).show()
//                    }
//
//                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
//                        Snackbar.make(
//                            binding.textView3,
//                            "Ayude con el permiso",
//                            Snackbar.LENGTH_LONG
//                        )
//                            .show()
//                    }
//
//                    false -> {
//                        Snackbar.make(binding.textView3, "permiso denegado", Snackbar.LENGTH_LONG)
//                            .show()
//                    }
//                }
//
//            }

        binding.btnSher.setOnClickListener {
            //----------------------------permiso---------
            locationContract.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            //----------------------------------------
            //Abre una url con un boton, este intent tiene un punto de partida pero no de llegada
            //con geo: se puede mandar la latitud y longitud de una pos del mapa
//            val intent = Intent(
//                Intent.ACTION_VIEW,
////                Uri.parse("https://developer.apple.com/")
////                Uri.parse("geo:-0.2006288,-78.5049638")
//                Uri.parse("tel:0123456789")
//            )
//            val intent = Intent(
//                Intent.ACTION_WEB_SEARCH
//            )
//            //Los parametros para abrir una aplicacion especifica
//            intent.setClassName(
//                "com.google.android.googlequicksearchbox",
//                "com.google.android.googlequicksearchbox.SearchActivity"
//            )
//            intent.putExtra(
//                SearchManager.QUERY,
//                "UCE"
//            )//es un query que me permite buscar algo determinado
//            startActivity(intent)

        }

        val appResultLocal =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultActivity ->

                val sn = Snackbar.make(
                    binding.textView3,
                    "",
                    Snackbar.LENGTH_LONG
                )

                var message = when (resultActivity.resultCode) {
                    RESULT_OK -> {
                        sn.setBackgroundTint(resources.getColor(R.color.amarillo))
//                        "resultado exitosos"
                        resultActivity.data?.getStringExtra("result").orEmpty()
//                        Log.d("UCE", "RESULTADO EXITOSO")
//                        Snackbar.make(binding.textView3,"RESULTADO EXITOSO",Snackbar.LENGTH_LONG).show()
                    }

                    RESULT_CANCELED -> {
                        sn.setBackgroundTint(resources.getColor(R.color.rojo_pasion))
//                        "resultado fallido"
                        resultActivity.data?.getStringExtra("result").orEmpty()
//                        Log.d("UCE", "RESULTADO FALLIDO")
//                        Snackbar.make(binding.textView3,"RESULTADO FALLIDO",Snackbar.LENGTH_LONG).show()
                    }

                    else -> {
                        "dudoso"
//                        Log.d("UCE", "RESULTADO DUDOSO")
//                        Snackbar.make(binding.textView3,"RESULTADO DUDOSO",Snackbar.LENGTH_LONG).show()
                    }
                }

                sn.setText(message)
                sn.show()

            }

        binding.btnResult.setOnClickListener {

            val intentSpeeach = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intentSpeeach.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intentSpeeach.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intentSpeeach.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "di algo"
            )

            speechToText.launch(intentSpeeach)
//            val resIntent = Intent(this, ResultActiviry::class.java)
//            appResultLocal.launch(resIntent)
        }


    }

    //parte del data store---------------
    private suspend fun saveDataStore(stringData: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("usuario")] = stringData
            prefs[stringPreferencesKey("session")] = UUID.randomUUID().toString()
            prefs[stringPreferencesKey("email")] = "sachango@uce.edu.ec"
        }
    }
    //-----------------------------

    private fun text() {
        var location = MyLocationManager(this)
        location.getUserLocation()
    }
}