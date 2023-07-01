package com.example.dispositivosmoviles.Proyecto.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dispositivosmoviles.databinding.FragmentSegundoBinding


class segundoFragment : Fragment() {
    private lateinit var binding: FragmentSegundoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSegundoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}