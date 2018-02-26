package me.paixao.tmdbexplorer.comm.interfaces


import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import me.paixao.tmdbexplorer.comm.ApiResponse
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbAPI {
    @GET("search/movie")
    fun search(@Query("api_key") apiKey: String,
               @Query("query") query: String): Observable<MovieList>

    @GET("discover/movie")
    fun discover(@Query("api_key") apiKey: String,
                 @Query("sort_by") query: String,
                 @Query("page") page: Int): LiveData<ApiResponse<MovieList>>

    @GET("movie/{movie_id}")
    fun get(@Path("movie_id") movieId: Long,
            @Query("api_key") apiKey: String): LiveData<ApiResponse<Movie?>>
}