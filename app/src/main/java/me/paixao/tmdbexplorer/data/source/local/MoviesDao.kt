package me.paixao.tmdbexplorer.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import me.paixao.tmdbexplorer.data.Movie

/**
 * Data Access Object for the movies table.
 */
@Dao
interface MoviesDao {

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    /**
     * Select a movie by id.
     *
     * @param movieId the movie id.
     * @return the movie with movieId.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Long): LiveData<Movie?>

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    /**
     * Update a movie.
     *
     * @param movie movie to be updated
     * @return the number of movies updated. This should always be 1.
     */
    @Update
    fun updateMovie(movie: Movie): Int

    /**
     * Delete a movie by id.
     *
     * @return the number of movies deleted. This should always be 1.
     */
    @Query("DELETE FROM movies WHERE id = :movieId")
    fun deleteMovieById(movieId: String): Int

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM movies")
    fun deleteMovies()
}