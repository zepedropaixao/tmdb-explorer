package me.paixao.tmdbexplorer.utils

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.example.android.architecture.blueprints.todoapp.util.AppExecutors
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.data.source.local.MoviesLocalDataSource
import me.paixao.tmdbexplorer.data.source.local.TMDBExplorerDatabase
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource
import me.paixao.tmdbexplorer.ui.moviedetail.MovieDetailViewModel
import me.paixao.tmdbexplorer.ui.movielist.MovieListViewModel


/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory private constructor(
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

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                            MoviesRepository
                                    .getInstance(MoviesRemoteDataSource.getInstance(AppExecutors()),
                                            MoviesLocalDataSource.getInstance(AppExecutors(), TMDBExplorerDatabase.getInstance(application.applicationContext).movieDao())))
                            .also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
