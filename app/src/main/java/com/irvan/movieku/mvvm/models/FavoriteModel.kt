package com.irvan.movieku.mvvm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite")
data class FavoriteModel(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,
    @SerializedName("session_id")
    @ColumnInfo(name = "session_id")
    var sessionId: String,
    @SerializedName("movie_id")
    @ColumnInfo(name = "movie_id")
    val movieId: String,
    @SerializedName("is_favorite")
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String,
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    var updatedAt: String
)