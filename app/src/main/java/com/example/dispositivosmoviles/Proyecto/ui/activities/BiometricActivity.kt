package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.Proyecto.ui.viewmodels.BiometricViewModel
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityBiometricBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiometricActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBiometricBinding

    private val biometricViewModel by viewModels<BiometricViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAutentication.setOnClickListener {
            autenticacionBiometric()
        }


        biometricViewModel.isLoading.observe(this){isLoading->
            if(isLoading){
                binding.main1.visibility=View.GONE
                binding.mainCopia.visibility=View.VISIBLE
            }else{
                binding.main1.visibility=View.VISIBLE
                binding.mainCopia.visibility=View.GONE
            }
        }

        lifecycleScope.launch{
            biometricViewModel.chargingData()
        }

    }

    private fun autenticacionBiometric() {

        if(checkBiometric()){
            //executor----------------------------------------
            val executor = ContextCompat.getMainExecutor(this)// es igual a una variable mainExecutor

            //aler dialog-----------------------------------------------------
            val biometricPrompt = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticacion Requerida")
                .setSubtitle("Ingrese con su huella digital")
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                //.setNegativeButtonText("Cancelar")
                .build()

            val biometricManager = BiometricPrompt(
                this,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(
                            Intent(
                                this@BiometricActivity,MainActivity::class.java
                            )
                        )
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })

            biometricManager.authenticate(biometricPrompt)
        }else{
            Snackbar.make(binding.btnAutentication,"No existen los requisitos necesarios",Snackbar.LENGTH_SHORT).show()

        }

    }

    private fun checkBiometric()  : Boolean{
        var returnValid:Boolean=false
        //BiometricManager controla todo lo que es el acceso al biometrico
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                returnValid= true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                returnValid= false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                returnValid= false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val intentPromp = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                intentPromp.putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
                startActivity( intentPromp)
                returnValid= false
            }
        }
        return returnValid
    }
}