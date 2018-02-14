package me.paixao.tmdbexplorer.ui.mainlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.comm.repositories.MovieRepository
import me.paixao.tmdbexplorer.comm.repositories.MovieRepositoryProvider

open class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()
    protected lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = MovieRepositoryProvider.provideRepository()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_movie_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings ->
                return true
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables?.clear()
    }
}
