package me.paixao.tmdbexplorer.ui.moviefile

import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.ui.mainlist.BaseActivity

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

                    Log.e("RESPONSE", "MY RESONSE: " + result)
                    repository.isLoadingData = false
                }, { error ->
                    error.printStackTrace()
                    Log.e("Error", error.message)
                    repository.isLoadingData = false
                }))
    }
}
