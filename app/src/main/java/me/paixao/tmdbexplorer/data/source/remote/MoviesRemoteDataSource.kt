package me.paixao.tmdbexplorer.data.source.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import io.reactivex.Observable
import me.paixao.tmdbexplorer.AppDelegate
import me.paixao.tmdbexplorer.comm.interfaces.TMDbAPI
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import me.paixao.tmdbexplorer.data.source.MoviesDataSource
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * Implementation of the data source of the network
 */
class MoviesRemoteDataSource : MoviesDataSource {

    private val apiKey = "83d01f18538cb7a275147492f84c3698"

    @Inject
    lateinit var retrofit: Retrofit

    var apiService: TMDbAPI

    init {
        AppDelegate.netComponent.inject(this)
        apiService = retrofit.create(TMDbAPI::class.java)
    }


    override fun getMovies(): LiveData<List<Movie>> {
        return getMovies(1)
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        val listMovies: LiveData<List<Movie>> = Transformations.map(
                apiService.discover(apiKey, "popularity.desc", pageNr), { data ->
            data.body?.results
        })
        return listMovies
    }

    override fun searchMovies(query: String): Observable<MovieList> {
        return apiService.search(apiKey, query)
    }

    override fun getMovie(movieId: Long): LiveData<Movie?> {
        val movie: LiveData<Movie?> = Transformations.map(
                apiService.get(movieId, apiKey), { data ->
            data.body
        })
        return movie
    }

    override fun refreshMovies() {
    }

    override fun deleteAllMovies() {
    }

    override fun deleteMovie(movieId: String) {
    }
}
