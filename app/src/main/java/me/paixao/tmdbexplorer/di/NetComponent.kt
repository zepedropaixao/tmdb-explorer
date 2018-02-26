package me.paixao.tmdbexplorer.di

import android.app.Application
import dagger.Component
import me.paixao.tmdbexplorer.data.source.remote.MoviesRemoteDataSource
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {
    fun inject(mrds: MoviesRemoteDataSource)

    fun app(): Application

    fun retrofit(): Retrofit
}