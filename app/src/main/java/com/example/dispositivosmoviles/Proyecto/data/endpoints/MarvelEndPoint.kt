package com.example.dispositivosmoviles.Proyecto.data.endpoints

import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.marvelAnime
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelEndPoint {
    @GET("characters")
    suspend fun getCharactersStartsWith(
        @Query("nameStartsWith") name: String,
        @Query("limit") limit: Int,
        @Query("ts") ts: String="uce1",
        @Query("apikey") apikey: String="48ed26ff242038147ce24450236a7ec2",
        @Query("hash") hash: String="e39fb11ad271b98d8cac028063ce639b"
    ): Response<marvelAnime>

}