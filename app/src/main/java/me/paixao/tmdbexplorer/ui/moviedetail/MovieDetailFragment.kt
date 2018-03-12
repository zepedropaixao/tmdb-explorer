package me.paixao.tmdbexplorer.ui.moviedetail

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.databinding.FragmentMovieDetailBinding
import me.paixao.tmdbexplorer.ui.BaseFragment
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.view.LayoutInflater



/**
 * Created by pedropaixao on 12/03/2018.
 */
class MovieDetailFragment : BaseFragment<MovieDetailActivity>() {
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: FragmentMovieDetailBinding

    override fun onCreateView(inflater: LayoutInflater?,
                              @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater!!, R.layout.fragment_movie_detail, container, false)
        val view = binding.root
        binding.setLifecycleOwner(this)
        initViewModel()
        return view
    }

    private fun initViewModel() {
        viewModel = (obtainViewModel() as MovieDetailViewModel)
        binding.viewmodel = viewModel
        viewModel.getMovieWithId(activity.intent.getLongExtra("movie_id", 1)).observe(activity, Observer<Movie?> { movie ->
            // Bind movie to view binder
            activity.title = movie?.title
            movie_rating.visibility = View.VISIBLE
        })
    }

}