package com.example.dispositivosmoviles.Proyecto.logic.marvellogic

import android.util.Log
import com.example.dispositivosmoviles.Proyecto.data.connections.ApiConnection
import com.example.dispositivosmoviles.Proyecto.data.endpoints.MarvelEndPoint
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars

class MarvelLogic {

    suspend fun getMarvelChars(name: String, limit: Int): ArrayList<MarvelChars> {

        //var call = ApiConnection.getJcanConnection()
        //val response = call.create(JikanEndpoint::class.java).getAllAnimes()

        var itemList = arrayListOf<MarvelChars>()

        val call = ApiConnection.getService(
            ApiConnection.typeApi.Marvel,
            MarvelEndPoint::class.java
        )

        if (call != null) {
            val response = call.getCharactersStartsWith(name, limit)
            Log.d("UCE", response.toString())
            if (response.isSuccessful) {
                response.body()!!.data.results.forEach {
                    var commic: String = "No available"
                    if (it.comics.items.size > 0) {
                        commic = it.comics.items[0].name
                    }

                    val m = MarvelChars(
                        it.id,
                        it.name,
                        commic,
                        it.thumbnail.path + "." + it.thumbnail.extension
                    )
                    itemList.add(m)
                }
            } else {
                Log.d("UCE", response.toString())
            }

        }
        return itemList

    }
}