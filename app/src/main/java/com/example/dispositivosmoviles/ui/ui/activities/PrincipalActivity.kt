package com.example.dispositivosmoviles.ui.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding
import com.example.dispositivosmoviles.ui.ui.fragment.FirstFragment
import com.example.dispositivosmoviles.ui.ui.fragment.segundoFragment
import com.example.dispositivosmoviles.ui.ui.fragment.terceroFragment
import com.example.dispositivosmoviles.ui.ui.utilities.FragmentManager

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
            when (item.itemId) {
                R.id.Inicio -> {
                    FragmentManager().replaceFragment(
                        supportFragmentManager,
                        binding.frContainer.id, FirstFragment()
                    )
                    /*val frag = FirstFragment()
                    val transacction = supportFragmentManager.beginTransaction()

                    transacction.add(
                        binding.frContainer.id,
                        frag
                    ) //necesitamos un contenedor (binding.frContainer.id) y un objeto (frag)
                    transacction.addToBackStack(null)
                    transacction.commit()
*/
                    true
                }

                R.id.Favorito -> {
                    FragmentManager().replaceFragment(
                        supportFragmentManager,
                        binding.frContainer2.id, segundoFragment()
                    )
                    //var frag1=segundoFragment()
                    //var transacction1=supportFragmentManager.beginTransaction()

//                transacction1.add(binding.frContainer2.id,frag1)
                    //               transacction1.addToBackStack(null)
                    //              transacction1.commit()

                    true
                }

                R.id.Chat_gpt -> {
                    FragmentManager().replaceFragment(
                        supportFragmentManager,
                        binding.frContainer.id, terceroFragment()
                    )
                   /* var frag2 = terceroFragment()
                    var transacction3 = supportFragmentManager.beginTransaction()
                    transacction3.add(binding.frContainer.id, frag2)
                    transacction3.addToBackStack(null)
                    transacction3.commit()
*/
                    true
                }

                else -> false
            }
        }


    }

    override fun onBackPressed() {
        super.onStart()
    }

}