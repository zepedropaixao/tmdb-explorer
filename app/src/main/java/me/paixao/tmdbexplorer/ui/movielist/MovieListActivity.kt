package me.paixao.tmdbexplorer.ui.movielist

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_movie_list.*
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.ui.moviefile.MovieFileActivity

open class MovieListActivity : BaseActivity() {

    private lateinit var viewModel: MovieListViewModel
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
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = obtainViewModel().apply {
            listOfMovies.observe(this@MovieListActivity, Observer<List<Movie>> { movieList ->
                if (viewModel.getPage() == 1) {
                    adapter.reset(movieList)
                } else {
                    adapter.addMovies(movieList)
                }
            })
            // Subscribe to "new task" event
            /*newTaskEvent.observe(this@MovieListActivity, Observer<Void> {
                this@MovieListActivity.addNewTask()
            })*/
        }
        if (!viewModel.completeListOfMovies.isEmpty())
            adapter.reset(viewModel.completeListOfMovies)
    }

    fun setupRecyclerView() {
        var spanCount = 3
        if (isLandscape()) spanCount = 6
        layoutManager = GridLayoutManager(this, spanCount)
        grid.layoutManager = layoutManager
        adapter = MovieListAdapter(mutableListOf())

        disposables.add(adapter.getViewClickedObservable()
                .subscribe({
                    val intent = Intent(this, MovieFileActivity::class.java)
                    intent.putExtra("movie_id", it.id)
                    startActivity(intent)
                }))

        grid.adapter = adapter
    }

    private fun setRecyclerViewScrollListener() {
        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if (!viewModel.isLoadingData() && totalItemCount <= lastVisibleItemPosition + 6)
                    viewModel.getMoreMovies()
            }
        })
    }

    fun obtainViewModel(): MovieListViewModel = obtainViewModel(MovieListViewModel::class.java)
}
