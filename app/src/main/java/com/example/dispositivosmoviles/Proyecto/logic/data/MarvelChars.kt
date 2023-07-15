package com.example.dispositivosmoviles.Proyecto.logic.data

import android.os.Parcelable
import com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData.database.MarvelCharsDB
import kotlinx.parcelize.Parcelize
@Parcelize
data class MarvelChars (
    var id:Int,
    var name:String,
    var comic:String,
    var imagen:String

    ):Parcelable
fun MarvelChars.getMarvelCharsDB():MarvelCharsDB{
    return MarvelCharsDB(
        id,
        name,
        comic,
        imagen
    )
}

