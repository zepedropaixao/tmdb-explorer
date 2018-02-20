package me.paixao.tmdbexplorer.data.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import io.reactivex.Observable
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import me.paixao.tmdbexplorer.data.source.local.MoviesLocalDataSource
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource
import me.paixao.tmdbexplorer.utils.addAllIfNotIn

/**
 * Concrete implementation to load movies from the data sources.
 */
class MoviesRepository(
        val moviesRemoteDataSource: MoviesRemoteDataSource,
        val moviesLocalDataSource: MoviesLocalDataSource
) : MoviesDataSource {

    var isLoadingData: Boolean = false
    var listOfMovies = MediatorLiveData<List<Movie>>()
    val myMovie = MediatorLiveData<Movie>()
    var completeList = mutableListOf<Movie>()
    var onlineArrivedFirst: Boolean = false
    var page: Int = 1

    init {
        getMovies()
    }

    /**
     * Gets movies from local data source (SQLite) or remote data source, whichever is
     * available first.
     */
    override fun getMovies(): LiveData<List<Movie>> {
        return getMovies(1)
    }

    fun getMoreMovies(): LiveData<List<Movie>> {
        page++
        return getMovies(page)
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        isLoadingData = true
        val localMovies: LiveData<List<Movie>> = moviesLocalDataSource.getMovies(pageNr)
        val remoteMovies: LiveData<List<Movie>> = moviesRemoteDataSource.getMovies(pageNr)
        listOfMovies.addSource(localMovies,
                { data ->
                    if (!onlineArrivedFirst && data != null) {
                        listOfMovies.value = data
                        completeList.addAllIfNotIn(data)
                    }
                    isLoadingData = false
                    listOfMovies.removeSource(localMovies)
                })
        listOfMovies.addSource(remoteMovies,
                { data ->
                    if (data != null) {
                        listOfMovies.value = data
                        completeList.addAllIfNotIn(data)
                        moviesLocalDataSource.saveMoviedOnDB(data)
                        onlineArrivedFirst = true
                    }

                    isLoadingData = false
                    listOfMovies.removeSource(remoteMovies)
                })
        return listOfMovies
    }

    override fun searchMovies(query: String): Observable<MovieList> {
        isLoadingData = true
        return moviesRemoteDataSource.searchMovies(query)
    }

    override fun getMovie(movieId: Long): LiveData<Movie?> {
        isLoadingData = true
        onlineArrivedFirst = false
        myMovie.addSource(moviesLocalDataSource.getMovie(movieId),
                { data ->
                    if (!onlineArrivedFirst && data != null) {
                        myMovie.value = data
                    }

                    isLoadingData = false
                })
        myMovie.addSource(moviesRemoteDataSource.getMovie(movieId),
                { data ->
                    if (data != null) {
                        myMovie.value = data
                        onlineArrivedFirst = true
                    }
                    isLoadingData = false
                })
        return myMovie
    }

    override fun refreshMovies() {

    }

    override fun deleteAllMovies() {
        moviesRemoteDataSource.deleteAllMovies()
        moviesLocalDataSource.deleteAllMovies()
    }

    override fun deleteMovie(movieId: String) {
        moviesRemoteDataSource.deleteMovie(movieId)
        moviesLocalDataSource.deleteMovie(movieId)
    }

    companion object {

        private var INSTANCE: MoviesRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param moviesRemoteDataSource the backend data source
         * *
         * @param moviesLocalDataSource  the device storage data source
         * *
         * @return the [MoviesRepository] instance
         */
        @JvmStatic
        fun getInstance(moviesRemoteDataSource: MoviesRemoteDataSource,
                        moviesLocalDataSource: MoviesLocalDataSource) =
                INSTANCE ?: synchronized(MoviesRepository::class.java) {
                    INSTANCE ?: MoviesRepository(moviesRemoteDataSource, moviesLocalDataSource)
                            .also { INSTANCE = it }
                }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
