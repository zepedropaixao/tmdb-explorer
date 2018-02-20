package me.paixao.tmdbexplorer.ui.moviedetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.utils.GlideApp
import me.paixao.tmdbexplorer.utils.format


/**
 * Exposes the data to be used in the movie detail screen.
 */
class MovieDetailViewModel(
        context: Application,
        private val moviesRepository: MoviesRepository
) : AndroidViewModel(context) {

    val movie: LiveData<Movie> = moviesRepository.myMovie

    val movieVoteAverage: LiveData<String> = Transformations.map(
            movie, { data ->
        "${data.vote_average} ${context.getString(R.string.vote_average)} (${data.vote_count} ${context.getString(R.string.total_votes)})"
    })

    val movieRating: LiveData<Int> = Transformations.map(
            movie, { data ->
        (data.vote_average * 10).toInt()
    })

    val movieDuration: LiveData<String> = Transformations.map(
            movie, { data ->
        "${data.runtime} ${context.getString(R.string.min)}"
    })

    val movieBudget: LiveData<String> = Transformations.map(
            movie, { data ->
        val revenue: Double = data.revenue.toDouble() / 1000000
        val budget: Double = data.budget.toDouble() / 1000000
        val budgetString = if (data.budget != 0) "(${context.getString(R.string.budget)}: \$${budget.format(2)}M)" else ""
        "${context.getString(R.string.revenue)}: \$${revenue.format(2)}M $budgetString"
    })

    val movieRevenueColor: LiveData<Int> = Transformations.map(
            movie, { data ->
        if (data.budget != 0)
            if (data.revenue > data.budget)
                context.resources.getColor(R.color.colorGreen)
            else context.resources.getColor(R.color.colorPrimary)
        else context.resources.getColor(android.R.color.white)
    })

    fun getMovieWithId(movieId: Long): LiveData<Movie> {
        moviesRepository.getMovie(movieId)
        return movie
    }

    fun isLoadingData(): Boolean {
        return moviesRepository.isLoadingData
    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:imagesrc")
        fun setImageSource(v: View, s: String?) {
            if (s != null)
                GlideApp.with(v.context)
                        .load("https://image.tmdb.org/t/p/w500$s")
                        .thumbnail(Glide.with(v.context).load(R.drawable.loader))
                        // TODO add image for error
                        .centerCrop()
                        .into(v as ImageView)
        }
    }

}
