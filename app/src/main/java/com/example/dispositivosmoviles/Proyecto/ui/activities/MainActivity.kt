package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
                lifecycleScope.launch(Dispatchers.IO){
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
    }

    //parte del data store---------------
    private suspend fun saveDataStore(stringData:String) {
        dataStore.edit {prefs->
            prefs[stringPreferencesKey("usuario")] = stringData
            prefs[stringPreferencesKey("session")] =UUID.randomUUID().toString()
            prefs[stringPreferencesKey("email")] ="sachango@uce.edu.ec"
        }
    }
    //-----------------------------
}