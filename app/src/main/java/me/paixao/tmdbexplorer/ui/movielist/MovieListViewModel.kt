package me.paixao.tmdbexplorer.ui.movielist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.BaseObservable
import android.databinding.Bindable
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.utils.SingleLiveEvent


/**
 * Exposes the data to be used in the movie list screen.
 *
 *
 * [BaseObservable] implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a [Bindable] annotation to the property's
 * getter method.
 */
class MovieListViewModel(
        context: Application,
        private val moviesRepository: MoviesRepository
) : AndroidViewModel(context) {

    var listOfMovies: LiveData<List<Movie>> = moviesRepository.listOfMovies

    val completeListOfMovies: List<Movie>
        get() = moviesRepository.completeList

    internal val openMovieEvent = SingleLiveEvent<String>()
    val snackbarMessage = SingleLiveEvent<Int>()

    fun getMovieList(): LiveData<List<Movie>>? {
        return listOfMovies
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
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
