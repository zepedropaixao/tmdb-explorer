package me.paixao.tmdbexplorer.ui.movielist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import me.paixao.tmdbexplorer.utils.ViewModelFactory

open class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>) =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

    fun isLandscape() = getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT

    override fun onDestroy() {
        super.onDestroy()
        disposables?.clear()
    }
}
