package me.paixao.tmdbexplorer.ui.movielist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import me.paixao.tmdbexplorer.data.source.MoviesRepository

/**
 * Exposes the data to be used in the movie list screen.
 */
class MovieListViewModel(
        context: Application,
        private val moviesRepository: MoviesRepository
) : AndroidViewModel(context) {

    var listOfMovies: LiveData<List<Movie>> = moviesRepository.listOfMovies

    val completeListOfMovies: List<Movie>
        get() = moviesRepository.completeList

    fun getMovieList() {
        moviesRepository.getMovies()
    }

    fun searchMovies(query: String): Observable<MovieList> {
        return moviesRepository.searchMovies(query)
    }

    fun getPage(): Int {
        return moviesRepository.page
    }

    fun getMoreMovies() {
        moviesRepository.getMoreMovies()
    }

    fun isLoadingData(): Boolean {
        return moviesRepository.isLoadingData
    }
}
