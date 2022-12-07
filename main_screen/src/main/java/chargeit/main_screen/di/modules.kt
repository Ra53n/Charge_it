package chargeit.main_screen.di

import chargeit.data.repository.LocalElectricStationRepo
import chargeit.data.repository.LocalElectricStationRepoImpl
import chargeit.main_screen.ui.filters.FiltersFragmentViewModel
import chargeit.main_screen.ui.maps.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    single<LocalElectricStationRepo> { LocalElectricStationRepoImpl(get()) }
    single { FiltersFragmentViewModel(get()) }
    viewModel { get() }
    viewModel { MapsFragmentViewModel(get(), get()) }
}