
object MovieRepositoryProvider {

    fun provideSearchRepository(): MovieRepository {
        return MovieRepository(TMDbAPI.Factory.create())
    }

}