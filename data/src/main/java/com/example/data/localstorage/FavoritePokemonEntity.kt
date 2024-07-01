package com.example.data.localstorage

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.utils.Constants.Companion.FAVORITE_POKEMOS_TABLE

@Entity(tableName = FAVORITE_POKEMOS_TABLE)
data class FavoritePokemonEntity (
    val name: String = "",
    @PrimaryKey(autoGenerate = false) val id: Int ? = null
)