package me.paixao.tmdbexplorer.comm.repositories

import me.paixao.tmdbexplorer.comm.interfaces.TMDbAPI

object MovieRepositoryProvider {

    fun provideSearchRepository(): MovieRepository {
        return MovieRepository(TMDbAPI.create())
    }

}