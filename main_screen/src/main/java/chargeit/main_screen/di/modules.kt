package chargeit.main_screen.di

import chargeit.data.repository.FakeElectricStationRepoImpl
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.main_screen.ui.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {

    single<LocalElectricStationRepo> { FakeElectricStationRepoImpl() }
    viewModel { MapsFragmentViewModel(get(), get()) }

}