package me.paixao.tmdbexplorer.data.source.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.VisibleForTesting
import com.example.android.architecture.blueprints.todoapp.util.AppExecutors
import me.paixao.tmdbexplorer.comm.interfaces.TMDbAPI
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.MoviesDataSource



/**
 * Implementation of the data source that adds a latency simulating network.
 */
class MoviesRemoteDataSource private constructor(
        val appExecutors: AppExecutors
) : MoviesDataSource {
    private val apiKey = "83d01f18538cb7a275147492f84c3698"
    private val apiService = TMDbAPI.create()

    private val data = MutableLiveData<List<Movie>>()




    /*fun discoverMoreMovies(): io.reactivex.Flowable<Result> {
        page++
        return apiService.discover(apiKey, "popularity.desc", page)
    }

    fun getMovie(movieId: Long): io.reactivex.Flowable<Movie> {
        return apiService.get(movieId, apiKey)
    }*/

    /**
     * Note: [LoadMoviesCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getMovies(): LiveData<List<Movie>> {
        val listMovies: LiveData<List<Movie>> = Transformations.map(
                apiService.discover(apiKey, "popularity.desc", 1), { data ->
            data.body?.results
        })
        return listMovies
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        TODO()
    }

    /**
     * Note: [GetMovieCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getMovie(movieId: Long): LiveData<Movie?> {
        val movie: LiveData<Movie?> = Transformations.map(
                apiService.get(movieId, apiKey), { data ->
            data.body
        })
        return movie
    }

    override fun refreshMovies() {
        // Not required because the {@link MoviesRepository} handles the logic of refreshing the
        // movies from all the available data sources.
    }

    override fun deleteAllMovies() {
    }

    override fun deleteMovie(movieId: String) {
    }

    companion object {
        private var INSTANCE: MoviesRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors): MoviesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(MoviesRemoteDataSource::javaClass) {
                    INSTANCE = MoviesRemoteDataSource(appExecutors)
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
