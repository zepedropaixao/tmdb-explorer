package me.paixao.tmdbexplorer.data.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.local.MoviesLocalDataSource
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource

/**
 * Concrete implementation to load movies from the data sources into a cache.
 *
 *
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
class MoviesRepository(
        val moviesRemoteDataSource: MoviesRemoteDataSource,
        val moviesLocalDataSource: MoviesLocalDataSource
) : MoviesDataSource {


    var isLoadingData: Boolean = false

    var listOfMovies = MediatorLiveData<List<Movie>>()

    var onlineArrivedFirst: Boolean = false

    var page: Int = 1

    init {
        initListOfMovies()
    }

    fun initListOfMovies() {
        isLoadingData = true
        listOfMovies.addSource(moviesLocalDataSource.getMovies(),
                { data ->
                    if (!onlineArrivedFirst) listOfMovies.value = data
                    isLoadingData = false
                })
        listOfMovies.addSource(moviesRemoteDataSource.getMovies(),
                { data ->
                    listOfMovies.value = data
                    onlineArrivedFirst = true
                    isLoadingData = false
                })
    }

    /**
     * Gets movies from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [LoadMoviesCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getMovies(): LiveData<List<Movie>> {
        return listOfMovies
    }

    fun getMoreMovies(): LiveData<List<Movie>> {
        page++
        return getMovies(page)
    }

    override fun getMovies(pageNr: Int): LiveData<List<Movie>> {
        moviesLocalDataSource.getMovies(pageNr)
        moviesRemoteDataSource.getMovies(pageNr)
        return listOfMovies
    }

    /**
     * Gets movies from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [GetMovieCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getMovie(movieId: Long): LiveData<Movie?> {
        val myMovie = MediatorLiveData<Movie?>()
        var onlineArrivedFirst: Boolean = false

        myMovie.addSource(moviesLocalDataSource.getMovie(movieId), { data -> if (!onlineArrivedFirst) myMovie.value = data })
        myMovie.addSource(moviesRemoteDataSource.getMovie(movieId),
                { data ->
                    myMovie.value = data
                    onlineArrivedFirst = true
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