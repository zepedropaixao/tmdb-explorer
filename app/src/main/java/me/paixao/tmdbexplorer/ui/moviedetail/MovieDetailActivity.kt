package me.paixao.tmdbexplorer.ui.moviedetail

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.databinding.ActivityMovieDetailBinding
import me.paixao.tmdbexplorer.ui.movielist.BaseActivity

class MovieDetailActivity : BaseActivity() {


    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.setLifecycleOwner(this)
        title = "moviExplorer"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initViewModel()


        /*disposables.add(repository.getMovieWithId(intent.getLongExtra("movie_id", 1))
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
                }))*/
    }

    private fun initViewModel() {
        viewModel = obtainViewModel()
        binding.viewmodel = viewModel
        viewModel.getMovieWithId(intent.getLongExtra("movie_id", 1)).observe(this@MovieDetailActivity, Observer<Movie?> { movie ->
            // Bind movie to view binder
            title = movie?.title
        })
    }


    fun obtainViewModel(): MovieDetailViewModel = obtainViewModel(MovieDetailViewModel::class.java)

}
