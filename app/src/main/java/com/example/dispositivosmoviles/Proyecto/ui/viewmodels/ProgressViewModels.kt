package com.example.dispositivosmoviles.Proyecto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
import com.example.dispositivosmoviles.Proyecto.logic.marvellogic.MarvelLogic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressViewModels : ViewModel() {

    val progressState = MutableLiveData<Int>()
    val items=MutableLiveData<MutableList<MarvelChars>>()

    fun processBackground(value: Long) {

        viewModelScope.launch(Dispatchers.Main) {
            val state = View.VISIBLE
            progressState.postValue(state)
            delay(value)
            val state1 = View.GONE
            progressState.postValue(state1)
        }
    }

    fun processBackground1() {

        val state = View.VISIBLE
        progressState.postValue(state)
        var total = 0
        for (i in 1 .. 300000) {
            total += i
        }

        val state1 = View.GONE
        progressState.postValue(state1)

    }

    suspend fun getMarvelChar(offset:Int,limit:Int){
        progressState.postValue(View.VISIBLE)

        val newItems=MarvelLogic().getAllMarvelChars(offset, limit)
        items.postValue(newItems)

        progressState.postValue(View.GONE)
    }
}