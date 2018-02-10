
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.recyclerview_movie_list_item.view.*
import me.paixao.tmdbexplorer.R



class RecyclerAdapter(private val movies: MutableList<Movie>) : RecyclerView.Adapter<RecyclerAdapter.MovieHolder>() {

    private val clickSubject = PublishSubject.create<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MovieHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_movie_list_item, false)
        return MovieHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.MovieHolder, position: Int) {
        val item = movies[position]
        holder.bindMovie(item)
    }

    override fun getItemCount() = movies.size

    fun getViewClickedObservable(): Observable<Movie> {
        val clickEvent: Observable<Movie> = clickSubject
        return clickEvent
    }

    fun reset(newMovies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun addMovies(newMovies: List<Movie>) {
        this.movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    inner class MovieHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var movie: Movie? = null

        init {
            v.setOnClickListener {
                clickSubject.onNext(movies[layoutPosition])
            }
        }

        fun bindMovie(movie: Movie) {
            this.movie = movie
            Glide.with(view.context).load("https://image.tmdb.org/t/p/w500"+movie.poster_path).into(view.itemImage)
            view.itemDate.text = movie.release_date
            view.itemDescription.text = movie.title
        }
    }
}