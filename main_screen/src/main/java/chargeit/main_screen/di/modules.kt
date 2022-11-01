package chargeit.main_screen.di

import chargeit.main_screen.settings.*
import chargeit.main_screen.view.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module

@OptIn(KoinReflectAPI::class)
val mainScreenModule = module {

    viewModel<MapsFragmentViewModel>()

}