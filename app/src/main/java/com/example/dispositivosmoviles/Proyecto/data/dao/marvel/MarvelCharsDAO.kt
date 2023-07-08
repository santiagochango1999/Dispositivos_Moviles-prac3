package com.example.dispositivosmoviles.Proyecto.data.dao.marvel

import androidx.room.Dao
import androidx.room.Index
import androidx.room.Insert
import androidx.room.Query
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.database.MarvelCharsDB

@Dao
interface MarvelCharsDAO {
    @Query("select * from MarvelCharsDB")
    fun getAllCharacters(): List<MarvelCharsDB>

    @Query("select * from MarvelCharsDB where id=:pk")
    fun getOneCharacter(pk: Int): MarvelCharsDB

    @Insert
    fun insertMarvelChar(ch: List<MarvelCharsDB>)
}