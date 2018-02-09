
object MovieRepositoryProvider {

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(TMDbAPI.Factory.create())
    }

}