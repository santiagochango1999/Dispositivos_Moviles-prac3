package com.example.dispositivosmoviles.ui.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.MarvelCharactersBinding
import com.example.dispositivosmoviles.ui.logic.entities.marvel.MarvelChars
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MarvelAdapter(private val items: List<MarvelChars>) :
    RecyclerView.Adapter<MarvelAdapter.MarvelViewHolder>() {

    class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: MarvelCharactersBinding = MarvelCharactersBinding.bind(view)

        fun render(item: MarvelChars) {
            binding.txtName.text = item.name //enlaso el texto con el comic
            binding.txtComic.text = item.comic
            Picasso.get().load(item.imagen).into(binding.imgmarvel)//enlaza la imagen

            //funcion
            binding.imgmarvel.setOnClickListener{
                Snackbar.make(binding.imgmarvel,item.name,Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelAdapter.MarvelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MarvelViewHolder(
            inflater.inflate(
                R.layout.marvel_characters,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MarvelAdapter.MarvelViewHolder, position: Int) {
        holder.render(items[position])
    }

    override fun getItemCount(): Int = items.size

}