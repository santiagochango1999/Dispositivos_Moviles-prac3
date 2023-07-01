package com.example.dispositivosmoviles.Proyecto.data.entities.marvel.marvelData

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)