package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.Proyecto.ui.viewmodels.ProgressViewModels
import com.example.dispositivosmoviles.databinding.ActivityProgressBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgressBinding
    private val progressviewmodel by viewModels<ProgressViewModels>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressviewmodel.progressState.observe(this, Observer {
            binding.progressBar.visibility = it
        })

        progressviewmodel.items.observe(this, Observer {
            Toast.makeText(this, it[0].name, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, NotificactionActivity::class.java))
        })
        binding.btnEnProceso.setOnClickListener {
            progressviewmodel.processBackground(3000)

        }
        binding.btnEnProceso1.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                progressviewmodel.getMarvelChar(0, 99)
            }

        }

    }
}