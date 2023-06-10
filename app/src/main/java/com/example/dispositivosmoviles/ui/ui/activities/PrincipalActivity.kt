package com.example.dispositivosmoviles.ui.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding
import com.example.dispositivosmoviles.ui.fragment.FirstFragment
import com.google.android.material.snackbar.Snackbar

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        Log.d("UCE", "Entrando a Create")
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        var name: String = ""
        //intent.extras?.let {
        //    name = it.getString("var1") ?: ""
        //}

        Log.d("UCE", "Hello $name")
        binding.textView.text = "Welcome $name!"

        Log.d("UCE", "Entrando a Start")
        initClass()
    }

    private fun initClass() {
        binding.imageButton2.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }


    binding.bottomNavigation.setOnItemReselectedListener { item ->
        when(item.itemId) {
            R.id.Inicio -> {
                val frag=FirstFragment()
                val transacction=supportFragmentManager.beginTransaction()
                transacction.add(binding.frContainer.id,frag) //necesitamos un contenedor (binding.frContainer.id) y un objeto (frag)
                transacction.addToBackStack(null)
                transacction.commit()

                true
            }
            R.id.Favorito -> {
                var suma:Int=0
                for(i in listOf(8,12,13)){
                    suma +=i
                }
                Snackbar.make(binding.textView,"la suma es ${suma}",Snackbar.LENGTH_LONG).show()
                true
            }

            R.id.Chat_gpt -> {
                // Respond to navigation item 2 click
                true
            }
            else -> false
        }
    }

    }
}