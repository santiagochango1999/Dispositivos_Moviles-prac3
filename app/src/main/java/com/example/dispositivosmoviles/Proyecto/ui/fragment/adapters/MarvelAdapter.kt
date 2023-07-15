package com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.MarvelCharactersBinding
import com.squareup.picasso.Picasso

class MarvelAdapter(
    private var items: List<MarvelChars>,
    private var fnClick: (MarvelChars) -> Unit,
    private var fnsave: (MarvelChars) -> Boolean
) :
    RecyclerView.Adapter<MarvelAdapter.MarvelViewHolder>() {
    class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val binding: MarvelCharactersBinding = MarvelCharactersBinding.bind(view)

        fun render(
            item: MarvelChars,
            fnClick: (MarvelChars) -> Unit,
            fnsave: (MarvelChars) -> Boolean

        ) {
            binding.txtName.text = item.name //enlaso el texto con el comic
            binding.txtComic.text = item.comic
            Picasso.get().load(item.imagen).into(binding.imgmarvel)//enlaza la imagen

            //funcion
            itemView.setOnClickListener {
                fnClick(item)
                //               Snackbar.make(binding.imgmarvel,item.name,Snackbar.LENGTH_SHORT).show() //lanza mensaje
            }
            binding.btsave.setOnClickListener{
                fnsave(item)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MarvelViewHolder(
            inflater.inflate(
                R.layout.marvel_characters,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MarvelViewHolder, position: Int) {
        holder.render(items[position], fnClick,fnsave)
    }

    override fun getItemCount(): Int = items.size

    fun updateListItems(newItems: List<MarvelChars>) {
        this.items = this.items.plus(newItems)//plus agrega a los nuevos elementos
        notifyDataSetChanged()
    }

    fun replaceListItems(newItems: List<MarvelChars>) {
        this.items = newItems
        notifyDataSetChanged()
    }

}