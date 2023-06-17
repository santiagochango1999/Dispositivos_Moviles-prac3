package com.example.dispositivosmoviles.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.ui.logic.validator.ListItems
import com.example.dispositivosmoviles.ui.ui.adapters.MarvelAdapter

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstBinding.inflate(
            layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val names= arrayListOf<String>("carlos","Xavier","Andres","Pepe")

        //
        //val adapter=ArrayAdapter<String>(
          //  requireActivity(),android.R.layout.simple_spinner_item
            //,names)

        val adapter=ArrayAdapter<String>(
            requireActivity(),R.layout.simple_spinner
            ,names)

        binding.spinnerFfirst.adapter = adapter

        val rvAdapter=MarvelAdapter(ListItems().returnMarvelChars())

        val rvMarvel=binding.rvMarvelChars
        rvMarvel.adapter=rvAdapter
        rvMarvel.layoutManager=LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false)

    }
}