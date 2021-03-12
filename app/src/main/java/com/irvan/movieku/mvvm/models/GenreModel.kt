package com.irvan.movieku.mvvm.models

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String
)