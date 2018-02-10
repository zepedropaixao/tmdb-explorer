package me.paixao.tmdbexplorer

import MovieRepository
import MovieRepositoryProvider
import RecyclerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var adapter: RecyclerAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val lastVisibleItemPosition: Int
        get() = layoutManager.findLastVisibleItemPosition()

    private lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSupportActionBar(toolbar)

        repository = MovieRepositoryProvider.provideSearchRepository()

        setupRecyclerView()
        setRecyclerViewScrollListener()

        /*RxView.clicks(button)
                .subscribe({ aVoid ->
                    //Perform some work here//
                })*/


    }

    fun setupRecyclerView() {

        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = GridLayoutManager(this, 3)
        } else {
            layoutManager = GridLayoutManager(this, 6)
        }
        grid.layoutManager = layoutManager
        adapter = RecyclerAdapter(mutableListOf())

        disposables.add(adapter.getViewClickedObservable()
                .subscribe({
                    Toast.makeText(this, "Clicked on ${it.title}", Toast.LENGTH_LONG).show()
                }))

        grid.adapter = adapter



        disposables.add(repository.discoverMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    adapter.reset(result.results)
                    repository.isLoadingData = false
                }, { error ->
                    error.printStackTrace()
                    Log.e("Error", error.message)
                    repository.isLoadingData = false
                }))
    }

    private fun setRecyclerViewScrollListener() {
        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                Log.e("ERROR", "IM HER "+totalItemCount+" - "+lastVisibleItemPosition)
                if (!repository.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    Log.e("ERROR", "IM HER")
                    disposables.add(repository.discoverMoreMovies()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                adapter.addMovies(result.results)
                                repository.isLoadingData = false
                            }, { error ->
                                error.printStackTrace()
                                Log.e("Error", error.message)
                                repository.isLoadingData = false
                            }))
                }
            }
        })
    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(3)
        } else {
            //show in six columns
            layoutManager.setSpanCount(6)
        }
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables?.clear()
    }
}
