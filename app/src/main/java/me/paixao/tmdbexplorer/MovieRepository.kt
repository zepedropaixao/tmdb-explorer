/**
 * Repository method to access search functionality of the api service
 */
class MovieRepository(val apiService: TMDbAPI) {

    val apiKey = "83d01f18538cb7a275147492f84c3698"

    var isLoadingData : Boolean = false

    var page: Int = 1

    fun discoverMovies(): io.reactivex.Flowable<Result> {
        isLoadingData = true
        return apiService.discover(apiKey, "popularity.desc",page)
    }

    fun discoverMoreMovies(): io.reactivex.Flowable<Result> {
        isLoadingData = true
        page++
        return apiService.discover(apiKey, "popularity.desc",page)
    }



}