package com.irvan.movieku.apis.apiresponses

import com.google.gson.annotations.SerializedName
import com.irvan.movieku.mvvm.models.GenreModel

data class GenreListResponse(
    @SerializedName("genres")
    val genres: MutableList<GenreModel>
)