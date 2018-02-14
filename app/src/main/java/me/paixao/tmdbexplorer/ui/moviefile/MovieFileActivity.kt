package me.paixao.tmdbexplorer.ui.moviefile

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_file.*
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.ui.mainlist.BaseActivity
import me.paixao.tmdbexplorer.utils.GlideApp
import me.paixao.tmdbexplorer.utils.format

class MovieFileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_file)
        title = "moviExplorer"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        disposables.add(repository.getMovie(intent.getLongExtra("movie_id", 1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    GlideApp.with(this)
                            .load("https://image.tmdb.org/t/p/w500${result.backdrop_path}")
                            .thumbnail(Glide.with(this).load(R.drawable.loader))
                            // TODO add image for error
                            .centerCrop()
                            .into(backdrop)
                    movie_title.text = result.title
                    title = result.title
                    movie_overview.text = result.overview
                    movie_vote_nr.text = "${result.vote_average} vote average (${result.vote_count} total votes)"
                    movie_rating.progress = (result.vote_average * 10).toInt()
                    movie_rating.visibility = View.VISIBLE
                    movie_duration.text = "${result.runtime} min"
                    val revenue: Double = result.revenue.toDouble() / 1000000
                    val budget: Double = result.budget.toDouble() / 1000000
                    val budgetString = if (result.budget != 0) "(Budget: \$${budget.format(2)}M)" else ""
                    movie_revenue.text = "Revenue: \$${revenue.format(2)}M $budgetString"
                    if (result.budget != 0)
                        if (result.revenue > result.budget)
                            movie_revenue.setTextColor(resources.getColor(R.color.colorGreen))
                        else movie_revenue.setTextColor(resources.getColor(R.color.colorPrimary))
                    repository.isLoadingData = false
                }, { error ->
                    error.printStackTrace()
                    Log.e("Error", error.message)
                    repository.isLoadingData = false
                }))
    }

}
