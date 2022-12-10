package chargeit.station_info.di

import chargeit.station_info.presentation.viewmodel.FullStationInfoViewModel
import chargeit.station_info.presentation.viewmodel.StationInfoBottomSheetViewModel
import org.koin.dsl.module

val stationInfoModule = module {
    single { StationInfoBottomSheetViewModel(get()) }
    single { FullStationInfoViewModel(get()) }
}