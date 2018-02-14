package me.paixao.tmdbexplorer.ui.mainlist

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_list.*
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.ui.moviefile.MovieFileActivity

open class MovieListActivity : BaseActivity() {

    private lateinit var adapter: MovieListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val lastVisibleItemPosition: Int
        get() = layoutManager.findLastVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setTitle("moviExplorer")
        setSupportActionBar(toolbar)

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
        adapter = MovieListAdapter(mutableListOf())

        disposables.add(adapter.getViewClickedObservable()
                .subscribe({
                    val intent = Intent(this, MovieFileActivity::class.java)
                    intent.putExtra("movie_id", it.id)
                    startActivity(intent)

                    //Toast.makeText(this, "Clicked on ${it.title}", Toast.LENGTH_LONG).show()
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
                if (!repository.isLoadingData && totalItemCount <= lastVisibleItemPosition + 6) {
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
}
