/**
 * Repository method to access search functionality of the api service
 */
class SearchRepository(val apiService: TMDbAPI) {

    val apiKey = "83d01f18538cb7a275147492f84c3698"

    fun discoverMovies(): io.reactivex.Flowable<Result> {
        return apiService.discover(apiKey, "popularity.desc",1)
    }



}