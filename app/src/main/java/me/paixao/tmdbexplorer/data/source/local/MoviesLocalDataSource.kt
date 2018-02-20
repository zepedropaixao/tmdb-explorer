package me.paixao.tmdbexplorer.data.source.local

import android.arch.lifecycle.LiveData
import android.support.annotation.VisibleForTesting
import io.reactivex.Observable
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import me.paixao.tmdbexplorer.data.source.MoviesDataSource
import me.paixao.tmdbexplorer.utils.AppExecutors


/**
 * Concrete implementation of a data source as a db.
 */
class MoviesLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val moviesDao: MoviesDao
) : MoviesDataSource {


    private val listOfMovies: LiveData<List<Movie>> = moviesDao.getMovies()

    override fun getMovies(): LiveData<List<Movie>> {
        return listOfMovies
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        return listOfMovies
    }

    override fun searchMovies(query: String): Observable<MovieList> {
        TODO("Search only works online")
    }

    fun saveMoviedOnDB(movies: List<Movie>) {
        appExecutors.diskIO.execute { moviesDao.insertMovies(movies) }
    }

    override fun getMovie(movieId: Long): LiveData<Movie?> {
        return moviesDao.getMovieById(movieId)
    }

    override fun refreshMovies() {
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
