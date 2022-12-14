package chargeit.main_screen.di

import chargeit.data.repository.FakeElectricStationRepoImpl
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.data.room.mappers.SocketModelToEntityMapper
import chargeit.main_screen.ui.filters.FiltersFragmentViewModel
import chargeit.main_screen.ui.maps.MapsFragmentViewModel
import chargeit.main_screen.utils.BitmapDescriptorUtils
import chargeit.main_screen.utils.GeocoderHelper
import chargeit.main_screen.utils.MapHelper
import chargeit.main_screen.utils.ViewHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    single { MapHelper(get()) }
    single { GeocoderHelper(get()) }
    single { ViewHelper() }
    single { BitmapDescriptorUtils(get()) }
    single { SocketModelToEntityMapper() }
    single<LocalElectricStationRepo> { FakeElectricStationRepoImpl() }
    single { FiltersFragmentViewModel() }
    viewModel { get() }
    viewModel { MapsFragmentViewModel(get(), get(), get(), get()) }
}