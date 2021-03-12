package com.irvan.movieku.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.irvan.movieku.mvvm.models.FavoriteModel

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAdmin(favorite: FavoriteModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAdmin(favorite: FavoriteModel)

    @Transaction
    @Query("SELECT * FROM favorite WHERE session_id = :session_id ORDER BY created_at DESC LIMIT :limit")
    fun getAdmin(limit: Int, session_id: String): LiveData<MutableList<FavoriteModel>>

    @Transaction
    @Query("SELECT * FROM favorite WHERE movie_id = :movie_id AND session_id = :session_id")
    fun getAdmin(movie_id: String, session_id: String): LiveData<FavoriteModel>

    @Query("UPDATE favorite SET is_favorite = :is_favorite WHERE movie_id = :movie_id AND session_id = :session_id")
    fun updateFav(movie_id: String, is_favorite: Boolean,  session_id: String)

}