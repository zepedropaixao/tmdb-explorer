package me.paixao.tmdbexplorer.ui.movielist

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.utils.addAllIfNotIn


class MovieListAdapter(private val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {

    private val clickSubject = PublishSubject.create<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.recyclerview_movie_list_item, parent, false)

        return MovieHolder(binding)

    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun getViewClickedObservable(): Observable<Movie> {
        val clickEvent: Observable<Movie> = clickSubject
        return clickEvent
    }

    fun reset(newMovies: List<Movie>?) {
        if (newMovies != null) {
            this.movies.clear()
            this.movies.addAll(newMovies)
            notifyDataSetChanged()
        }
    }

    fun addMovies(newMovies: List<Movie>?) {
        if (newMovies != null) {
            this.movies.addAllIfNotIn(newMovies)
            notifyDataSetChanged()
        }
    }

    inner class MovieHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                clickSubject.onNext(movies[layoutPosition])
            }
        }

        fun bind(movie: Movie) {
            binding.setVariable(BR.viewmodel, MovieListItemViewModel(binding.root.context, movie))
            binding.executePendingBindings()
        }
    }
}


