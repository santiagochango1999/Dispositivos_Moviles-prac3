package com.example.dispositivosmoviles.Proyecto.data.connections

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConnection {

    //con la enum podemos escojer la una o la otra en este caso solo tenemos dos opciones
    enum class typeApi {
        Jikan, Marvel
    }

    private val API_JIKAN = "https://api.jikan.moe/v4/"
    private val API_MARVEL = "https://gateway.marvel.com/v1/public/"

    private fun getConnection(base: String): Retrofit {
        //esto me ayuda a conectarme a una URL base
        var retrofit = Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())// para utilizar Gson ConverterFactory toca importar a mano la linea 4
            .build()
        return retrofit
    }

    //para crear un servicio
    suspend fun <T, E : Enum<E>> getService(Api: E, service: Class<T>): T {
        var BASE = ""
        when (Api.name) {
            typeApi.Jikan.name -> {
                BASE = API_JIKAN
            }

            typeApi.Marvel.name -> {
                BASE = API_MARVEL
            }
        }
        return getConnection(BASE).create(service)
    }

}