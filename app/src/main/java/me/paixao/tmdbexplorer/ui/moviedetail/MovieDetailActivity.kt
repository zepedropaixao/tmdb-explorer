package me.paixao.tmdbexplorer.ui.moviedetail

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_movie_detail.*
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
    }

    private fun initViewModel() {
        viewModel = obtainViewModel()
        binding.viewmodel = viewModel
        viewModel.getMovieWithId(intent.getLongExtra("movie_id", 1)).observe(this@MovieDetailActivity, Observer<Movie?> { movie ->
            // Bind movie to view binder
            title = movie?.title
            movie_rating.visibility = View.VISIBLE
        })
    }


    fun obtainViewModel(): MovieDetailViewModel = obtainViewModel(MovieDetailViewModel::class.java)

}
