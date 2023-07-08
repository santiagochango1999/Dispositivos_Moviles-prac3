package com.example.dispositivosmoviles.Proyecto.logic.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class MarvelChars (
    var id:Int,
    var name:String,
    var comic:String,
    var imagen:String

    ):Parcelable


