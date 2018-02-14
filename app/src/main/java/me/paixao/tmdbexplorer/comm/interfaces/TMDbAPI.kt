package me.paixao.tmdbexplorer.comm.interfaces

import io.reactivex.Flowable
import me.paixao.tmdbexplorer.models.Movie
import me.paixao.tmdbexplorer.models.Result
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbAPI {

    @GET("search/movie")
    fun search(@Query("api_key") apiKey: String,
               @Query("query") query: String,
               @Query("page") page: Int): Flowable<Result>

    @GET("discover/movie")
    fun discover(@Query("api_key") apiKey: String,
                 @Query("sort_by") query: String,
                 @Query("page") page: Int): Flowable<Result>

    @GET("movie/{movie_id}")
    fun get(@Path("movie_id") movieId: Long,
            @Query("api_key") apiKey: String): Flowable<Movie>


    /**
     * Companion object to create the TMDbAPI
     */
    companion object Factory {
        fun create(): TMDbAPI {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/")
                    .build()

            return retrofit.create(TMDbAPI::class.java)
        }
    }
}