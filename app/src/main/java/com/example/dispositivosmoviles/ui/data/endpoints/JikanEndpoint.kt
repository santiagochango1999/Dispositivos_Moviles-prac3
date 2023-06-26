package com.example.dispositivosmoviles.ui.data.endpoints

import com.example.dispositivosmoviles.ui.data.entities.jikan.JikanAnimeEntity
import retrofit2.Response
import retrofit2.http.GET

interface JikanEndpoint {
    @GET("top/anime")
    suspend fun getAllAnimes(): Response<com.example.dispositivosmoviles.ui.data.entities.jikan.JikanAnimeEntity>
}