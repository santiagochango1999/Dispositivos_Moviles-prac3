package com.example.dispositivosmoviles.Proyecto.ui.utilities

import android.app.Application
import androidx.room.Room
import com.example.dispositivosmoviles.Proyecto.data.connections.MarvelConneccionDB
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.database.MarvelCharsDB

//esta clase va a estar atada al ciclo de vida y solo puede haber uno
class DispositivosMoviles : Application() {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            MarvelConneccionDB::class.java,
            "marvelDB"
        ).build()

    }

    //un companion object es un objeto que se crea dentro de una clase
    companion object {
        private var db: MarvelConneccionDB? = null
        fun getDbInstance(): MarvelConneccionDB {
            return db!!
        }

    }
}