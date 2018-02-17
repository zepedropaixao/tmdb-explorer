package me.paixao.tmdbexplorer.ui.moviedetail

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
class MovieDetailViewModel(
        context: Application,
        private val moviesRepository: MoviesRepository
) : AndroidViewModel(context) {

    val movie: LiveData<Movie?> = moviesRepository.myMovie

    val snackbarMessage = SingleLiveEvent<Int>()

    fun getMovieWithId(movieId: Long): LiveData<Movie?> {
        moviesRepository.getMovie(movieId)
        return movie
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
    }

    fun isLoadingData(): Boolean {
        return moviesRepository.isLoadingData
    }
}
