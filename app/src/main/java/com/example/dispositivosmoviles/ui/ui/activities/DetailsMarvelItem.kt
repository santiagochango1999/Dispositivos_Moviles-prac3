package com.example.dispositivosmoviles.ui.ui.activities

import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityDetailsMarvelItemBinding
import com.example.dispositivosmoviles.ui.data.entities.marvel.MarvelChars
import com.squareup.picasso.Picasso

class DetailsMarvelItem : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsMarvelItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsMarvelItemBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_details_marvel_item)
    }

    override fun onStart() {
        super.onStart()

        /*
        var name: String? = ""
        intent.extras?.let {
            name = it.getString("name")

        }
        if (!name.isNullOrEmpty()) {
            binding.txtName.text = name
        }*/


        val item=intent.getParcelableExtra<MarvelChars>("name")

        if (item != null) {
            binding.txtName.text = item.name
            Picasso.get().load(item.imagen).into(binding.imageMarvel)
        }

    }
}