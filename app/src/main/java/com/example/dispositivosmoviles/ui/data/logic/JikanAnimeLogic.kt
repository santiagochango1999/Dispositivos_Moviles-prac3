package com.example.dispositivosmoviles.ui.data.logic

import com.example.dispositivosmoviles.ui.data.connections.ApiConnection
import com.example.dispositivosmoviles.ui.data.endpoints.JikanEndpoint
import com.example.dispositivosmoviles.ui.data.entities.marvel.MarvelChars

class JikanAnimeLogic {

    suspend fun getAllanimes(): List<MarvelChars> {

        var call = ApiConnection.getJcanConnection()
        val response = call.create(JikanEndpoint::class.java).getAllAnimes()

        var itemList = arrayListOf<MarvelChars>()
        if (response.isSuccessful) {
            response.body()!!.data.forEach {

                val m = MarvelChars(
                    it.mal_id,
                    it.title,
                    it.images.jpg.image_url,
                    it.titles[0].title
                )
                itemList.add(m)
            }
        }
        return itemList
    }
}