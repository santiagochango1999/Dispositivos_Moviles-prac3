package com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars
//import androidx.room.Entity
//import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MarvelCharsDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var comic: String,
    var imagen: String
) :Parcelable

fun MarvelCharsDB.getMarvelChars(): MarvelChars{
    return  MarvelChars(
        id,
        name,
        comic,
        imagen,
    )
}

