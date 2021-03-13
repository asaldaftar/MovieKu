package com.irvan.movieku.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.irvan.movieku.mvvm.models.GenreModel


@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genreModel: GenreModel)

    @Query("UPDATE genre SET name = :name WHERE id = :id")
    fun updateGenre(id: String, name: String)

    @Query("SELECT * FROM genre ORDER BY name ASC")
    fun getGenre(): LiveData<MutableList<GenreModel>>

    @Query("SELECT * FROM genre WHERE name LIKE '%' || :name || '%' LIMIT 3")
    fun getGenre(name: String): LiveData<MutableList<GenreModel>>

    @Query("SELECT * FROM genre WHERE id = :id")
    fun getGenreById(id: String): GenreModel
}