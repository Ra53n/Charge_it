package chargeit.main_screen.di

import chargeit.main_screen.ui.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {

    viewModel { MapsFragmentViewModel(get()) }

}