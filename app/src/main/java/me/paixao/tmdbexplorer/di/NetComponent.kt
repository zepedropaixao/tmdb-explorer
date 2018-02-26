package me.paixao.tmdbexplorer.di

import dagger.Component
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetModule::class, ViewModelFactoryModule::class])
interface NetComponent {
    fun inject(mrds: MoviesRemoteDataSource)
}