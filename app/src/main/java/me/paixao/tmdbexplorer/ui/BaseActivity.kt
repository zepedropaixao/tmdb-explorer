package me.paixao.tmdbexplorer.ui

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import me.paixao.tmdbexplorer.AppDelegate
import me.paixao.tmdbexplorer.utils.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    @Inject
    protected lateinit var vmf: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AppDelegate.vmfComponent.inject(this)
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

    abstract fun obtainViewModel(): Any

    fun <T : AndroidViewModel> obtainViewModel(viewModelClass: Class<T>) =
            ViewModelProviders.of(this, vmf).get(viewModelClass)

    fun isLandscape() = getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT

    override fun onDestroy() {
        super.onDestroy()
        disposables?.clear()
    }
}