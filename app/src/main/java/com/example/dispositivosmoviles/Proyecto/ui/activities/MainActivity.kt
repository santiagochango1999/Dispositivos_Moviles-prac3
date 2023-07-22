package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PermissionResult
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.Proyecto.data.validator.LoginValidator
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.Permission
import java.util.Locale
import java.util.UUID

//parte del DATA STORE
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")// es una percostencia temporal
//---------------------


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // (Lateinit) un gran poder conlleva una gran responsabilidad

    //---------- localicacion del usuario
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //-------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //-------------------------variable de location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        initClass()
        //val db = DispositivosMoviles.getDbInstance()
        //db.marvelDao()

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
        val locationContract =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                when (isGranted) {
                    true -> {
                        val task = fusedLocationProviderClient.lastLocation

                        task.addOnSuccessListener {
                            if (task.result != null) {
                                Snackbar.make(
                                    binding.textView3,
                                    "${it.latitude},${it.longitude}",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            } else {
                                Snackbar.make(
                                    binding.textView3,
                                    "encienda el gps porfavor",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
//                    Snackbar.make(binding.textView3, "permiso concedido", Snackbar.LENGTH_LONG).show()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                        Snackbar.make(
                            binding.textView3,
                            "Ayude con el permiso",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }

                    false -> {
                        Snackbar.make(binding.textView3, "permiso denegado", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

            }
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
        val speechToText =
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
}