package chargeit.main_screen.di

import chargeit.data.repository.FakeElectricStationRepoImpl
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.main_screen.ui.filters.FiltersFragmentViewModel
import chargeit.main_screen.ui.maps.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    single<LocalElectricStationRepo> { FakeElectricStationRepoImpl() }
    single { FiltersFragmentViewModel() }
    viewModel { get() }
    viewModel { MapsFragmentViewModel(get(), get()) }
}