package com.example.dispositivosmoviles.Proyecto.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
import com.example.dispositivosmoviles.Proyecto.logic.data.getMarvelCharsDB
import com.example.dispositivosmoviles.Proyecto.logic.marvellogic.MarvelLogic
import com.example.dispositivosmoviles.Proyecto.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.Proyecto.ui.activities.dataStore
import com.example.dispositivosmoviles.Proyecto.ui.data.UserDataStore
import com.example.dispositivosmoviles.Proyecto.ui.fragment.adapters.MarvelAdapter
import com.example.dispositivosmoviles.Proyecto.ui.utilities.DispositivosMoviles
import com.example.dispositivosmoviles.Proyecto.ui.utilities.Metodos
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var lmanager: LinearLayoutManager
    private lateinit var gManager: GridLayoutManager
    private lateinit var rvAdapter: MarvelAdapter
//    private var rvAdapter: MarvelAdapter  MarvelAdapter{sendMarvelItem(it)}

    private val limit = 99
    private var offset = 0
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
        gManager = GridLayoutManager(requireActivity(), 2)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        //parte del data store
        lifecycleScope.launch(Dispatchers.IO){
            getDataStore().collect{user->
                Log.d("UCE",user.name)
                Log.d("UCE",user.email)
                Log.d("UCE",user.session)
                //binding.textfilder.text=it.toString()
            }
        }
        //-----------------------

//----------------------------------------------------------------------------------NOMBRES
        val names = arrayListOf<String>("carlos", "Xavier", "Andres", "Pepe")
        val adapter = ArrayAdapter<String>(
            requireActivity(), R.layout.simple_spinner, names
        )
        binding.spinnerFfirst.adapter = adapter
//------------------------------------------------------------------------------

        chargeDataRVInit(limit = limit, offset = offset)

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRVAPI(limit = limit, offset = offset)
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
                                val items = MarvelLogic().getAllMarvelChars(offset, limit)
//                                val newItems = JikanAnimeLogic().getAllanimes()
                                /* val newItems = MarvelLogic().getMarvelChars(
                                    name = "spider",
                                    limit = 5
                                )*/
                                withContext(Dispatchers.Main) {
                                    rvAdapter.updateListItems(items)
                                    this@FirstFragment.offset += limit
                                }

                            }
                        }
                    }
                }
            })

        //nos sirve para filtrar la informacion
        binding.textfilder.addTextChangedListener { textfilter ->
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


    fun chargeDataRVAPI(limit: Int, offset: Int) {
        //val rvAdapter = MarvelAdapter(
        //  ListItems().returnMarvelChars()
        //) { sendMarvelItem(it) }//entre llaves se manda los lambdas

        lifecycleScope.launch(Dispatchers.Main) {
            //cambiamos a IO porque era una coneccion de dato
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext (MarvelLogic().getAllMarvelChars(offset, limit))
//                return@withContext (MarvelLogic().getMarvelChars(search,99))
            }
            rvAdapter = MarvelAdapter(
                marvelCharsItems,
                fnClick = { sendMarvelItem(it) },
                fnsave = { saveMarvelItem(it) }
            )

            //rvAdapter.items = JikanAnimeLogic().getAllanimes()

            binding.rvMarvelChars.apply {
                this.adapter = rvAdapter
                //lo reemplazo por otro manager
                this.layoutManager = lmanager

            }
            this@FirstFragment.offset += limit

        }
    }

    fun chargeDataRVInit(limit: Int, offset: Int) {

        //como no tengo contexto le llamo al requeryActivity
        if (Metodos().isOnline(requireActivity())) {
            lifecycleScope.launch(Dispatchers.Main) {
                marvelCharsItems = withContext(Dispatchers.IO) {

//                var items = MarvelLogic().getAllMarvelCharsDB().toMutableList()
//
//                if (items.isEmpty()) {
//                    items =(MarvelLogic().getAllMarvelChars(0, 99))
//                    MarvelLogic().insertMarvelCharstoDB(items)
//                }
                    return@withContext MarvelLogic().getInitChar(limit, offset)
                }

                rvAdapter = MarvelAdapter(
                    marvelCharsItems,
                    fnClick = { sendMarvelItem(it) },
                    fnsave = { saveMarvelItem(it) }
                )

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter
                    //lo reemplazo por otro manager
                    this.layoutManager = lmanager
                }
            }
//        page++
        } else {
            Snackbar.make(
                binding.CardView,
                "No hay coneccion",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    //parte de la Data Store
    private fun getDataStore():Flow<UserDataStore>{
        val user=requireActivity().dataStore.data.map { prefs ->
            UserDataStore(
                name=prefs[stringPreferencesKey("usuario")].orEmpty(),
                email = prefs[stringPreferencesKey("email")].orEmpty(),
                session = prefs[stringPreferencesKey("session")].orEmpty()
                //.orEmpty() bos sirve para entregar lo del punto para atras o sino devuelve falso
            )
        }
        return user
    }

    //-----------------------

}