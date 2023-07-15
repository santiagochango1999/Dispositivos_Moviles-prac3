package com.example.dispositivosmoviles.Proyecto.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
import com.example.dispositivosmoviles.Proyecto.logic.data.getMarvelCharsDB
import com.example.dispositivosmoviles.Proyecto.logic.jikanlogic.JikanAnimeLogic
import com.example.dispositivosmoviles.Proyecto.logic.marvellogic.MarvelLogic
import com.example.dispositivosmoviles.Proyecto.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters.MarvelAdapter
import com.example.dispositivosmoviles.Proyecto.ui.utilities.DispositivosMoviles
import com.example.dispositivosmoviles.databinding.FragmentSegundoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class segundoFragment : Fragment() {
    private lateinit var binding: FragmentSegundoBinding
    private lateinit var lmanager: LinearLayoutManager
    private lateinit var rvAdapter: MarvelAdapter

    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf<MarvelChars>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSegundoBinding.inflate(
            layoutInflater, container, false
        )
        lmanager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        return binding.root
    }

    private fun sendMarvelItem(item: MarvelChars) {
        //Los intents solo se encuentran en los fragments y en las Activities
        val i = Intent(requireActivity(), DetailsMarvelItem::class.java)
        // Esta forma de enviar informacion es ineficiente cuando se tienen mas atributos
        i.putExtra("name", item)
//        i.putExtra("name", item.name)
//        i.putExtra("comic", item.comic)
        startActivity(i)
    }

    fun saveMarvelItem(item: MarvelChars): Boolean {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                DispositivosMoviles
                    .getDbInstance()
                    .marvelDao()
                    .insertMarvelChar(listOf(item.getMarvelCharsDB()))
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        //carga los datos
//        chargeDataRV("cap")

        binding.rvSwipe.setOnRefreshListener {
//            chargeDataRV("cap")
            binding.rvSwipe.isRefreshing = false
        }
        binding.rvMarvelChars.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val v = lmanager.childCount
                    val p = lmanager.findFirstVisibleItemPosition()
                    val t = lmanager.itemCount
                    if ((v + p) >= t) {
                        lifecycleScope.launch((Dispatchers.IO)) {
                            val newItems = JikanAnimeLogic().getAllanimes()
                            withContext(Dispatchers.Main) {
                                rvAdapter.updateListItems(newItems)
                            }
                        }
                    }
                }
            }
        })

        //nos sirve para filtrar informacion
        binding.textfilder.addTextChangedListener {
            chargeDataRV(binding.textfilder.text.toString())
        }

    }

    private fun chargeDataRV(search: String) {

        lifecycleScope.launch(Dispatchers.Main) {
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext (MarvelLogic().getMarvelChars(
                    search, 99
                ))
            }
            rvAdapter = MarvelAdapter(marvelCharsItems, fnClick = { sendMarvelItem(it) },  fnsave = { saveMarvelItem(it)})
            binding.rvMarvelChars.apply {
                this.adapter = rvAdapter
                this.layoutManager = lmanager
            }
        }
    }
}