package com.example.dispositivosmoviles.Proyecto.data.logic.jikanlogic

import com.example.dispositivosmoviles.Proyecto.data.connections.ApiConnection
import com.example.dispositivosmoviles.Proyecto.data.endpoints.JikanEndpoint
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.MarvelChars

class JikanAnimeLogic {

    suspend fun getAllanimes(): List<MarvelChars> {

        //var call = ApiConnection.getJcanConnection()
        //val response = call.create(JikanEndpoint::class.java).getAllAnimes()

        var itemList = arrayListOf<MarvelChars>()

        val response = ApiConnection.getService(
            ApiConnection.typeApi.Jikan,
            JikanEndpoint::class.java
        ).getAllAnimes()

        if (response.isSuccessful) {
            response.body()!!.data.forEach {

                val m = MarvelChars(
                    it.mal_id,
                    it.title,
                    it.titles[0].title,
                    it.images.jpg.image_url,
                )
                itemList.add(m)
            }
        }
        return itemList
    }
}