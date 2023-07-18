package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityDetailsMarvelItemBinding
import com.example.dispositivosmoviles.databinding.ActivityResultActiviryBinding

class ResultActiviry : AppCompatActivity() {

    private lateinit var binding: ActivityResultActiviryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultActiviryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.btnResult.setOnClickListener {

            setResult(RESULT_OK)
            finish()
        }
        binding.btnFlase.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}