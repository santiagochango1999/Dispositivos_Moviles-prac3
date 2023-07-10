package com.example.dispositivosmoviles.Proyecto.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
import com.example.dispositivosmoviles.Proyecto.logic.marvellogic.MarvelLogic
import com.example.dispositivosmoviles.Proyecto.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters.MarvelAdapter
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var lmanager: LinearLayoutManager
    private lateinit var gManager:GridLayoutManager
    private lateinit var rvAdapter: MarvelAdapter
//    private var rvAdapter: MarvelAdapter  MarvelAdapter{sendMarvelItem(it)}

    private var page = 1
    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf<MarvelChars>()

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
        gManager=GridLayoutManager(requireActivity(),2)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
//----------------------------------------------------------------------------------NOMBRES
        val names = arrayListOf<String>("carlos", "Xavier", "Andres", "Pepe")
        val adapter = ArrayAdapter<String>(
            requireActivity(), R.layout.simple_spinner, names
        )
        binding.spinnerFfirst.adapter = adapter
//------------------------------------------------------------------------------

        chargeDataRV("")

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRV("")
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
                                val items = MarvelLogic().getAllMarvelChars(0, 99)
//                                val newItems = JikanAnimeLogic().getAllanimes()
                                /* val newItems = MarvelLogic().getMarvelChars(
                                    name = "spider",
                                    limit = 5
                                )*/
                                withContext(Dispatchers.Main) {
                                    rvAdapter.updateListItems(items)
                                }

                            }
                        }
                    }
                }
            })

        //nos sirve para filtrar la informacion
        binding.textfilder.addTextChangedListener {
                textfilter ->
            var newItems = marvelCharsItems.filter { items ->
                items.name.lowercase().contains(textfilter.toString().lowercase())
            }
            rvAdapter.replaceListItems(newItems)
//            chargeDataRV(binding.textfilder.text.toString())
        }
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

        lifecycleScope.launch(Dispatchers.Main) {
            //cambiamos a IO porque era una coneccion de dato
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext (MarvelLogic().getAllMarvelChars(0, 99))
//                return@withContext (MarvelLogic().getMarvelChars(search,99))
            }
            rvAdapter = MarvelAdapter(
                marvelCharsItems,
                fnClick = { sendMarvelItem(it) }
            )

            //rvAdapter.items = JikanAnimeLogic().getAllanimes()

            binding.rvMarvelChars.apply {
                this.adapter = rvAdapter
                //lo reemplazo por otro manager
                this.layoutManager = lmanager

            }

        }
    }
}