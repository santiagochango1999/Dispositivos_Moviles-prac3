package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.Proyecto.data.validator.LoginValidator
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

//parte del DATA STORE
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")// es una percostencia temporal

//---------------------
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // (Lateinit) un gran poder conlleva una gran responsabilidad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initClass()
        //val db = DispositivosMoviles.getDbInstance()
        //db.marvelDao()

    }

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
        binding.btnSher.setOnClickListener {
            //Abre una url con un boton, este intent tiene un punto de partida pero no de llegada
            //con geo: se puede mandar la latitud y longitud de una pos del mapa
//            val intent = Intent(
//                Intent.ACTION_VIEW,
////                Uri.parse("https://developer.apple.com/")
////                Uri.parse("geo:-0.2006288,-78.5049638")
//                Uri.parse("tel:0123456789")
//            )
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
                "UCE"
            )//es un query que me permite buscar algo determinado
            startActivity(intent)
        }

        val appResultLocal =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultActivity ->
                when (resultActivity.resultCode) {
                    RESULT_OK -> {
//                        Log.d("UCE", "RESULTADO EXITOSO")
                        Snackbar.make(binding.textView3,"RESULTADO EXITOSO",Snackbar.LENGTH_LONG).show()
                    }

                    RESULT_CANCELED -> {
                        Log.d("UCE", "RESULTADO FALLIDO")
                        Snackbar.make(binding.textView3,"RESULTADO FALLIDO",Snackbar.LENGTH_LONG).show()
                    }

                    else -> {
                        Log.d("UCE", "RESULTADO DUDOSO")
                        Snackbar.make(binding.textView3,"RESULTADO DUDOSO",Snackbar.LENGTH_LONG).show()
                    }
                }

//                if (resultActivity.resultCode == RESULT_OK) {
//
//                }else{
//                    if(resultActivity.resultCode == RESULT_CANCELED){
//
//                    }else{
//
//                    }
//                }

            }
        binding.btnResult.setOnClickListener {

            val resIntent = Intent(this, ResultActiviry::class.java)
            appResultLocal.launch(resIntent)
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