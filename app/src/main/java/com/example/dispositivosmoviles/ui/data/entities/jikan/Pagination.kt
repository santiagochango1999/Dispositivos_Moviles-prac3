package com.example.dispositivosmoviles.ui.data.entities.jikan

data class Pagination(
    val current_page: Int,
    val has_next_page: Boolean,
    val items: com.example.dispositivosmoviles.ui.data.entities.jikan.Items,
    val last_visible_page: Int
)