package com.example.dispositivosmoviles.Proyecto.data.endpoints

import retrofit2.Response
import retrofit2.http.GET

interface JikanEndpoint {

    @GET("top/anime")
    suspend fun getAllAnimes(): Response<com.example.dispositivosmoviles.Proyecto.data.entities.jikan.JikanAnimeEntity>
}