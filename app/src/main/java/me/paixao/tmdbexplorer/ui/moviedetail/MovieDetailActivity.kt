package me.paixao.tmdbexplorer.ui.moviedetail

import android.os.Bundle
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.ui.BaseActivity
import me.paixao.tmdbexplorer.utils.addFragment

class MovieDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        addFragment(MovieDetailFragment(), R.id.fragment_container)
    }

    override fun obtainViewModel() = obtainViewModel(MovieDetailViewModel::class.java)
}
