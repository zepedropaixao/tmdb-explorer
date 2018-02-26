package me.paixao.tmdbexplorer.di

import android.app.Application
import dagger.Module
import dagger.Provides
import me.paixao.tmdbexplorer.data.source.MoviesRepository
import me.paixao.tmdbexplorer.data.source.local.MoviesLocalDataSource
import me.paixao.tmdbexplorer.data.source.local.TMDBExplorerDatabase
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource
import me.paixao.tmdbexplorer.utils.AppExecutors
import me.paixao.tmdbexplorer.utils.ViewModelFactory
import javax.inject.Singleton


@Module
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun providesMoviesRepository(app: Application): MoviesRepository {
        return MoviesRepository(MoviesRemoteDataSource(),
                MoviesLocalDataSource.getInstance(AppExecutors(), TMDBExplorerDatabase.getInstance(app.applicationContext).movieDao()))
    }

    @Provides
    @Singleton
    fun providesViewModelFactory(app: Application, repo: MoviesRepository): ViewModelFactory {
        return ViewModelFactory(app, repo)
    }
}