package me.paixao.tmdbexplorer.data.source

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList

/**
 * Main entry point for accessing Movies data.
 */
interface MoviesDataSource {

    fun getMovies(): LiveData<List<Movie>>

    fun getMovies(pageNr: Int): LiveData<List<Movie>>

    fun searchMovies(query: String): Observable<MovieList>

    fun getMovie(movieId: Long): LiveData<Movie?>

    fun refreshMovies()

    fun deleteAllMovies()

    fun deleteMovie(movieId: String)
}