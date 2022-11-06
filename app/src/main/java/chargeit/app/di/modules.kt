package chargeit.app.di

import chargeit.app.repository.*
import org.koin.dsl.module

val appModule = module {

    single<LocalCarRepo> { LocalCarRepoImpl(get()) }
    single<LocalElectricStationRepo> { LocalElectricStationRepoImpl(get()) }
    single<LocalSocketRepo> { LocalSocketRepoImpl(get()) }
    single<LocalUserRepo> { LocalUserRepoImpl(get()) }
}