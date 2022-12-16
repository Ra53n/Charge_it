package chargeit.station_info.di

import chargeit.data.interactor.ElectricStationInteractor
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.data.repository.LocalElectricStationRepoImpl
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.mappers.SocketModelToEntityMapper
import chargeit.station_info.presentation.viewmodel.FullStationInfoViewModel
import chargeit.station_info.presentation.viewmodel.StationInfoBottomSheetViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val stationInfoModule = module {
    factory { SocketModelToEntityMapper() }
    factory { ElectricStationModelToEntityMapper(get()) }
    single<LocalElectricStationRepo>(named("localElectricStationRepo")) { LocalElectricStationRepoImpl(get(),get()) }
    factory { ElectricStationInteractor(get(named("localElectricStationRepo"))) }
    single { StationInfoBottomSheetViewModel(get(),get()) }
    single { FullStationInfoViewModel(get(),get()) }
}