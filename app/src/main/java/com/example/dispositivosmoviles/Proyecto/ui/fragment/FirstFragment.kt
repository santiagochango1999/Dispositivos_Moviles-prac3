package com.example.dispositivosmoviles.Proyecto.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.MarvelChars
import com.example.dispositivosmoviles.Proyecto.data.logic.jikanlogic.JikanAnimeLogic
import com.example.dispositivosmoviles.Proyecto.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters.MarvelAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstBinding.inflate(
            layoutInflater, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val names = arrayListOf<String>("carlos", "Xavier", "Andres", "Pepe")

        //
        //val adapter=ArrayAdapter<String>(
        //  requireActivity(),android.R.layout.simple_spinner_item
        //,names)

        val adapter = ArrayAdapter<String>(
            requireActivity(), R.layout.simple_spinner, names
        )

        binding.spinnerFfirst.adapter = adapter

        chargeDataRV()

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRV()
            binding.rvSwipe.isRefreshing = false
        }//ayuda a cargar con el rvswipe


    }

    fun sendMarvelItem(item: MarvelChars) {
        val i = Intent(requireActivity(), DetailsMarvelItem::class.java)
        i.putExtra("name", item)
        startActivity(i) //para iniciar la actividad
    }

    fun chargeDataRV() {
        //val rvAdapter = MarvelAdapter(
        //  ListItems().returnMarvelChars()
        //) { sendMarvelItem(it) }//entre llaves se manda los lambdas

        lifecycleScope.launch(Dispatchers.IO) {
            val rvAdapter = MarvelAdapter(
                JikanAnimeLogic().getAllanimes()
            ) { sendMarvelItem(it) }//entre llaves se manda los lambdas

            withContext(Dispatchers.Main) {
                with(binding.rvMarvelChars) {
                    this.adapter = rvAdapter
                    this.layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
        }


    }
}