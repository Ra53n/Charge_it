package chargeit.app

import android.app.Application
import chargeit.app.di.appModule
import chargeit.main_screen.di.mainScreenModule
import chargeit.profilescreen.di.profileScreenModule
import chargeit.station_info.di.stationInfoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, mainScreenModule, profileScreenModule, stationInfoModule)
        }
    }
}