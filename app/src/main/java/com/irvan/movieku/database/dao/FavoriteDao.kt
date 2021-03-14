package com.irvan.movieku.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.irvan.movieku.mvvm.models.FavoriteModel

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorit(favorite: FavoriteModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorit(favorite: FavoriteModel)

    @Transaction
    @Query("SELECT * FROM favorite WHERE session_id = :session_id ORDER BY movie_id DESC")
    fun getFavorit(session_id: String): LiveData<MutableList<FavoriteModel>>

    @Transaction
    @Query("SELECT * FROM favorite WHERE movie_id = :movie_id AND session_id = :session_id")
    fun getFavorit(movie_id: String, session_id: String): LiveData<FavoriteModel>

    @Query("UPDATE favorite SET is_favorite = :is_favorite WHERE movie_id = :movie_id AND session_id = :session_id")
    fun updateFav(movie_id: String, is_favorite: Boolean,  session_id: String)

    @Query("SELECT COUNT(*) FROM favorite WHERE session_id = :session_id AND movie_id = :movie_id LIMIT 1")
    fun checkFav(session_id: String, movie_id: String): Int

    @Query("SELECT COUNT(*) FROM favorite WHERE session_id = :session_id AND movie_id = :movie_id AND is_favorite = :fav LIMIT 1")
    fun checkIfFavorite(session_id: String, movie_id: String, fav: Boolean = true): Int

}