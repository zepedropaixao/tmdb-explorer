package me.paixao.tmdbexplorer.models

data class Movie(
        val id: Long,
        val poster_path: String,
        val backdrop_path: String?,
        val adult: Boolean,
        val overview: String,
        val release_date : String,
        val original_title : String,
        val original_language : String,
        val title : String,
        val popularity : Double,
        val vote_count : Int,
        val runtime : Int?,
        val revenue : Int,
        val budget : Int,
        val video : Boolean,
        val vote_average : Double
)

data class Result (val page: Int,
                   val total_pages: Int,
                   val total_results: Int,
                   val results: List<Movie>)