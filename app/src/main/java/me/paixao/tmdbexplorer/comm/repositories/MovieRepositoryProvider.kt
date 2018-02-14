package me.paixao.tmdbexplorer.comm.repositories

import me.paixao.tmdbexplorer.comm.interfaces.TMDbAPI

object MovieRepositoryProvider {

    fun provideRepository(): MovieRepository {
        return MovieRepository(TMDbAPI.create())
    }

}