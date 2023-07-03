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
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.MarvelChars
import com.example.dispositivosmoviles.Proyecto.data.logic.jikanlogic.JikanAnimeLogic
import com.example.dispositivosmoviles.Proyecto.data.logic.marvellogic.MarvelLogic
import com.example.dispositivosmoviles.Proyecto.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters.MarvelAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var lmanager: LinearLayoutManager
    private var rvAdapter: MarvelAdapter= MarvelAdapter { sendMarvelItem(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstBinding.inflate(
            layoutInflater, container, false
        )

        lmanager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
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

        chargeDataRV("cap")

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRV("cap")
            binding.rvSwipe.isRefreshing = false
        }//ayuda a cargar con el rvswipe

        //addOnScrollListener()  ES EL EVENTO QUE ME AYUDA A CONTAR
        binding.rvMarvelChars.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val v = lmanager.childCount//cuantos han pasado
                        val p = lmanager.findFirstVisibleItemPosition()//cual es mi posicion actual
                        val t = lmanager.itemCount//total

                        if ((v + p) >= t) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val newItems = JikanAnimeLogic().getAllanimes()
                                /* val newItems = MarvelLogic().getMarvelChars(
                                    name = "spider",
                                    limit = 5
                                )*/
                                withContext(Dispatchers.Main) {
                                    rvAdapter.updateListItems(newItems)
                                }

                            }
                        }
                    }
                }
            })


    }

    fun sendMarvelItem(item: MarvelChars) {
        val i = Intent(requireActivity(), DetailsMarvelItem::class.java)
        i.putExtra("name", item)
        startActivity(i) //para iniciar la actividad
    }


    fun chargeDataRV(search: String) {
        //val rvAdapter = MarvelAdapter(
        //  ListItems().returnMarvelChars()
        //) { sendMarvelItem(it) }//entre llaves se manda los lambdas

        lifecycleScope.launch(Dispatchers.IO) {

            rvAdapter.items = JikanAnimeLogic().getAllanimes()

            withContext(Dispatchers.Main) {

                with(binding.rvMarvelChars) {
                    this.adapter = rvAdapter
                    this.layoutManager = lmanager
                    /*this.layoutManager=LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )*/
                }
            }
        }


    }
}