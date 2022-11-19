package chargeit.app.di

import chargeit.data.repository.*
import chargeit.data.room.mappers.CarModelToEntityMapper
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.mappers.UserModelToEntityMapper
import org.koin.dsl.module

val appModule = module {

    single<LocalCarRepo> { LocalCarRepoImpl(get()) }
    single<LocalElectricStationRepo> { LocalElectricStationRepoImpl(get()) }
    single<LocalSocketRepo> { LocalSocketRepoImpl() }
    single<LocalUserRepo> { LocalUserRepoImpl(get()) }

    single { CarModelToEntityMapper() }
    single { ElectricStationModelToEntityMapper() }
    single { UserModelToEntityMapper(get()) }

}