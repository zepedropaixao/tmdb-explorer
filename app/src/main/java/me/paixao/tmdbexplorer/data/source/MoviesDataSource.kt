package me.paixao.tmdbexplorer.data.source

import android.arch.lifecycle.LiveData
import me.paixao.tmdbexplorer.data.Movie

/**
 * Main entry point for accessing Movies data.
 *
 *
 * For simplicity, only getMovies() and getMovieWithId() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new Movie is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
interface MoviesDataSource {

    fun getMovies(): LiveData<List<Movie>>

    fun getMovies(pageNr : Int): LiveData<List<Movie>>

    fun getMovie(movieId: Long): LiveData<Movie?>

    fun refreshMovies()

    fun deleteAllMovies()

    fun deleteMovie(MovieId: String)
}