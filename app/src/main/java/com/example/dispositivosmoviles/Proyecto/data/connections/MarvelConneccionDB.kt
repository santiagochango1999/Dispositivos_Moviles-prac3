package com.example.dispositivosmoviles.Proyecto.data.connections

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dispositivosmoviles.Proyecto.data.dao.marvel.MarvelCharsDAO
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.database.MarvelCharsDB

@Database(
    entities = [MarvelCharsDB::class],
    version = 1
)
abstract class MarvelConneccionDB :RoomDatabase(){

    abstract fun marvelDao() : MarvelCharsDAO


}