package me.paixao.tmdbexplorer.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import me.paixao.tmdbexplorer.data.Movie

/**
 * The Room Database that contains the Movie table.
 */
@Database(entities = arrayOf(Movie::class), version = 1)
abstract class TMDBExplorerDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    companion object {

        private var INSTANCE: TMDBExplorerDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): TMDBExplorerDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            TMDBExplorerDatabase::class.java, "Movies.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}