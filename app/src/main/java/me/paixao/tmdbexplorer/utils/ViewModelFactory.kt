package me.paixao.tmdbexplorer.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.ui.moviedetail.MovieDetailViewModel
import me.paixao.tmdbexplorer.ui.movielist.MovieListViewModel


/**
 * A creator is used to inject the product ID into the ViewModel
 */
class ViewModelFactory constructor(
        private val application: Application,
        private val moviesRepository: MoviesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(MovieListViewModel::class.java) ->
                        MovieListViewModel(application, moviesRepository)
                    isAssignableFrom(MovieDetailViewModel::class.java) ->
                        MovieDetailViewModel(application, moviesRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
