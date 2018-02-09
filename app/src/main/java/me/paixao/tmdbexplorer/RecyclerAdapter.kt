import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recyclerview_movie_list_item.view.*
import me.paixao.tmdbexplorer.R

class RecyclerAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<RecyclerAdapter.MovieHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MovieHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_movie_list_item, false)
        return MovieHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.MovieHolder, position: Int) {
        val item = movies[position]
        holder.bindMovie(item)
    }

    override fun getItemCount() = movies.size

    class MovieHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var movie: Movie? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val MOVIE_KEY = "MOVIE"
        }

        fun bindMovie(movie: Movie) {
            this.movie = movie
            Glide.with(view.context).load("https://image.tmdb.org/t/p/w500"+movie.poster_path).into(view.itemImage)
            view.itemDate.text = movie.release_date
            view.itemDescription.text = movie.title
        }
    }
}