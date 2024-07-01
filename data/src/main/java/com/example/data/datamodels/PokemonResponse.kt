package com.example.data.datamodels

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("count") val count: Long,
    @SerializedName("next") val next: String = "",
    @SerializedName("previous") val previous: String = "",
    @SerializedName("results") val results: ArrayList<Pokemon>,
)
