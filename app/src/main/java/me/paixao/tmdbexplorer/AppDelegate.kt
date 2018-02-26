package me.paixao.tmdbexplorer

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import me.paixao.tmdbexplorer.di.*

class AppDelegate : Application() {

    companion object {
        lateinit var instance: AppDelegate
            private set

        lateinit var netComponent: NetComponent
        lateinit var vmfComponent: ViewModelFactoryComponent
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        netComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(NetModule("https://api.themoviedb.org/3/"))
                .build()

        vmfComponent = DaggerViewModelFactoryComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }


}