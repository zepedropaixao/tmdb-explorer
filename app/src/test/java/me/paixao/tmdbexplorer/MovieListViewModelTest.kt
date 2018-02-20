package me.paixao.tmdbexplorer

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.res.Resources
import com.google.common.collect.Lists
import me.paixao.tmdbexplorer.data.Movie
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.ui.movielist.MovieListViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MovieListViewModelTest {

    // Executes each movie synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var moviesRepository: MoviesRepository
    @Mock
    private lateinit var context: Application
    @Mock
    private lateinit var moviesViewModel: MovieListViewModel
    private lateinit var movies: List<Movie>

    private var moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    @Mock
    private var moviesMediatorLiveData: MediatorLiveData<List<Movie>> = MediatorLiveData()

    @Before
    fun setupMoviesViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        setupContext()


        // We initialise the movies to 3, with one active and two completed
        val movie1 = Movie(1, "/img.jpg", null, false,
                "bla bla", "2017-05-09", "title_orig", "en",
                "title", 50.0, 3569, 123, 654, 12,
                false, 5.3)
        val movie2 = Movie(2, "/img.jpg", null, false,
                "bla bla", "2017-05-09", "title_orig", "en",
                "title", 50.0, 3569, 123, 654, 12,
                false, 5.3)
        val movie3 = Movie(3, "/img.jpg", null, false,
                "bla bla", "2017-05-09", "title_orig", "en",
                "title", 50.0, 3569, 123, 654, 12,
                false, 5.3)

        movies = Lists.newArrayList(movie1, movie2, movie3)
        moviesMediatorLiveData.addSource(moviesLiveData,
                { data ->
                    moviesMediatorLiveData.value = data

                })
        moviesLiveData.value = movies

        `when`(moviesRepository.getMovies()).thenReturn(moviesMediatorLiveData)
        `when`(moviesRepository.listOfMovies).thenReturn(moviesMediatorLiveData)
        `when`(moviesRepository.completeList).thenReturn(mutableListOf(movie1, movie2, movie3))


        // Get a reference to the class under test
        moviesViewModel = MovieListViewModel(context, moviesRepository)

    }

    private fun setupContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)

        `when`(context.resources).thenReturn(Mockito.mock(Resources::class.java))


    }

    @Test
    fun loadAllMoviesFromRepository_dataLoaded() {
        with(moviesViewModel) {

            // And data loaded
            assertFalse(completeListOfMovies.isEmpty())
            assertTrue(completeListOfMovies.size == 3)
        }
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}