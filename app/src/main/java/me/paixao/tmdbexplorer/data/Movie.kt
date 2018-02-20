package me.paixao.tmdbexplorer.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


/**
 * Immutable model class for a Movie.
 */
@Entity(tableName = "movies")
data class Movie(
        @PrimaryKey @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "poster_path") val poster_path: String,
        @ColumnInfo(name = "backdrop_path") val backdrop_path: String?,
        @ColumnInfo(name = "adult") val adult: Boolean,
        @ColumnInfo(name = "overview") val overview: String,
        @ColumnInfo(name = "release_date") val release_date: String,
        @ColumnInfo(name = "original_title") val original_title: String,
        @ColumnInfo(name = "original_language") val original_language: String,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "popularity") val popularity: Double,
        @ColumnInfo(name = "vote_count") val vote_count: Int,
        @ColumnInfo(name = "runtime") val runtime: Int?,
        @ColumnInfo(name = "revenue") val revenue: Int,
        @ColumnInfo(name = "budget") val budget: Int,
        @ColumnInfo(name = "video") val video: Boolean,
        @ColumnInfo(name = "vote_average") val vote_average: Double
) {
    val hasBudget
        get() = budget != 0

    val hasRevenue
        get() = revenue != 0
}

data class MovieList(val page: Int,
                  val total_pages: Int,
                  val total_results: Int,
                  val results: List<Movie>)