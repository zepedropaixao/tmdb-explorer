package me.paixao.tmdbexplorer.di

import dagger.Component
import me.paixao.tmdbexplorer.ui.BaseActivity

@ForActivity
@Component(modules = [ViewModelFactoryModule::class], dependencies = [NetComponent::class])
interface ViewModelFactoryComponent {
    fun inject(activity: BaseActivity)
}