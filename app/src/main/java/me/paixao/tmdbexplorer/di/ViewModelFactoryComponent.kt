package me.paixao.tmdbexplorer.di

import dagger.Component
import me.paixao.tmdbexplorer.ui.BaseActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, ViewModelFactoryModule::class])
interface ViewModelFactoryComponent {
    fun inject(activity: BaseActivity)
}