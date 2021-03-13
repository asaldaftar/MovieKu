package com.irvan.movieku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.database.dao.FavoriteDao
import com.irvan.movieku.database.dao.GenreDao
import com.irvan.movieku.mvvm.models.FavoriteModel
import com.irvan.movieku.mvvm.models.GenreModel


@Database(
    entities = [FavoriteModel::class, GenreModel::class],
    version = 1
)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun genreDao(): GenreDao
    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = if (!BuildConfig.DEBUG) {
                            Room.databaseBuilder(
                                context.applicationContext, AppDatabase::class.java,
                                BuildConfig.DATABASE_NAME
                            )
                                .setJournalMode(JournalMode.TRUNCATE)
                                .fallbackToDestructiveMigration()
                                .build()
                        } else {
                            Room.databaseBuilder(
                                context.applicationContext, AppDatabase::class.java,
                                BuildConfig.DATABASE_NAME
                            )
                                .fallbackToDestructiveMigration()
                                .build()
                        }
                    }
                }
            }
            return instance
        }
    }
}