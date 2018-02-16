package me.paixao.tmdbexplorer.comm.interfaces


import android.arch.lifecycle.LiveData
import me.paixao.tmdbexplorer.BuildConfig
import me.paixao.tmdbexplorer.comm.ApiResponse
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.MovieList
import me.paixao.tmdbexplorer.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbAPI {

    @GET("search/movie")
    fun search(@Query("api_key") apiKey: String,
               @Query("query") query: String,
               @Query("page") page: Int): LiveData<ApiResponse<MovieList>>

    @GET("discover/movie")
    fun discover(@Query("api_key") apiKey: String,
                 @Query("sort_by") query: String,
                 @Query("page") page: Int): LiveData<ApiResponse<MovieList>>

    @GET("movie/{movie_id}")
    fun get(@Path("movie_id") movieId: Long,
            @Query("api_key") apiKey: String): LiveData<ApiResponse<Movie?>>


    /**
     * Companion object to create the TMDbAPI
     */
    companion object Factory {
        fun create(): TMDbAPI {

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()

            return retrofit.create(TMDbAPI::class.java)
        }
    }
}