package com.irvan.movieku.mvvm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite")
data class FavoriteModel(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @SerializedName("session_id")
    @ColumnInfo(name = "session_id")
    var sessionId: String,
    @SerializedName("movie_id")
    @ColumnInfo(name = "movie_id")
    val movieId: String,
    @SerializedName("movie_cover")
    @ColumnInfo(name = "movie_cover")
    val movie_cover: String? = null,
    @SerializedName("movie_title")
    @ColumnInfo(name = "movie_title")
    val movie_title: String,
    @SerializedName("is_favorite")
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)