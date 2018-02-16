package me.paixao.tmdbexplorer.data.source.local

import android.arch.lifecycle.LiveData
import android.support.annotation.VisibleForTesting
import com.example.android.architecture.blueprints.todoapp.util.AppExecutors
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.MoviesDataSource


/**
 * Concrete implementation of a data source as a db.
 */
class MoviesLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val moviesDao: MoviesDao
) : MoviesDataSource {

    private val listOfMovies: LiveData<List<Movie>> = moviesDao.getMovies()

    /**
     * Note: [LoadMoviesCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getMovies(): LiveData<List<Movie>> {
        return listOfMovies
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        return listOfMovies
    }

    /**
     * Note: [GetMovieCallback.onDataNotAvailable] is fired if the [Movie] isn't
     * found.
     */
    override fun getMovie(movieId: Long): LiveData<Movie?> {
        return moviesDao.getMovieById(movieId)
    }

    override fun refreshMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // movies from all the available data sources.
    }

    override fun deleteAllMovies() {
        appExecutors.diskIO.execute { moviesDao.deleteMovies() }
    }

    override fun deleteMovie(movieId: String) {
        appExecutors.diskIO.execute { moviesDao.deleteMovieById(movieId) }
    }

    companion object {
        private var INSTANCE: MoviesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, moviesDao: MoviesDao): MoviesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MoviesLocalDataSource::javaClass) {
                    INSTANCE = MoviesLocalDataSource(appExecutors, moviesDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
