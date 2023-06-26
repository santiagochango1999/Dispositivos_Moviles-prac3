package com.example.dispositivosmoviles.ui.data.connections

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConnection {

    fun getJcanConnection(): Retrofit {
        //esto me ayuda a conectarme a una URL base
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())// para utilizar Gson ConverterFactory toca importar a mano la linea 4
            .build()
        return retrofit
    }

}