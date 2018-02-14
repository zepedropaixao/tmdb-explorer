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

class MovieFileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_file)
        setTitle("moviExplorer")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        disposables.add(repository.getMovie(intent.getLongExtra("movie_id", 1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result.backdrop_path != null)
                        GlideApp.with(this)
                                .load("https://image.tmdb.org/t/p/w500" + result.backdrop_path)
                                .thumbnail(Glide.with(this).load(R.drawable.loader))
                                .centerCrop()
                                .into(backdrop)
                    movie_title.text = result.title
                    setTitle(result.title)
                    movie_overview.text = result.overview
                    movie_vote_nr.text = "${result.vote_average} vote average (${result.vote_count} total votes)"
                    movie_rating.progress = (result.vote_average * 10).toInt()
                    movie_rating.visibility = View.VISIBLE
                    repository.isLoadingData = false
                }, { error ->
                    error.printStackTrace()
                    Log.e("Error", error.message)
                    repository.isLoadingData = false
                }))
    }
}
