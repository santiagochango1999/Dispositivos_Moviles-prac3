package com.example.dispositivosmoviles.Proyecto.data.validator

import com.example.dispositivosmoviles.Proyecto.data.entities.LoginUser
import com.example.dispositivosmoviles.Proyecto.logic.data.MarvelChars

class ListItems {
    fun returnItems(): List<LoginUser> {
        var items = listOf<LoginUser>(
            LoginUser("1", "1"),
            LoginUser("2", "1"),
            LoginUser("3", "1"),
            LoginUser("4", "1"),
            LoginUser("5", "1"),
        )
        return items
    }

    fun returnMarvelChars(): List<MarvelChars> {
        val items = listOf(
            MarvelChars(
                1,
                "Wolvering",
                "Wolvering(2021)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/5/57023/7469590-wolverinerb.jpg"
            ),
            MarvelChars(
                2,
                "Spider-Man",
                "The Amazing Spider-Man",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8126579-amazing_spider-man_vol_5_54_stormbreakers_variant_textless.jpg"
            ),

            MarvelChars(
                3,
                "Thor",
                "Thor",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8632242-rco009_1653501316.jpg"
            ),

            MarvelChars(
                4,
                "Iron Man",
                "Iron Man",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/8654427-ezgif-1-2f113089e4.jpg"
            ),

            MarvelChars(
                5,
                "Hulk",
                "Hulk",
            "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/7892286-immortal_hulk_vol_1_38_.jpg"
            ),
        )
        return items
    }
}