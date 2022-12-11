package chargeit.profilescreen.di

import chargeit.data.interactor.CarInteractor
import chargeit.data.interactor.UserInteractor
import chargeit.data.repository.LocalCarRepo
import chargeit.data.repository.LocalCarRepoImpl
import chargeit.profilescreen.data.mapper.UserMapper
import chargeit.profilescreen.viewmodel.LoginViewModel
import chargeit.profilescreen.viewmodel.ProfileRegistrationViewModel
import chargeit.profilescreen.viewmodel.ProfileViewModel
import chargeit.profilescreen.viewmodel.SocketSelectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileScreenModule = module {
    single<LocalCarRepo> { LocalCarRepoImpl(get(), get()) }
    factory { CarInteractor(get()) }
    factory { UserMapper() }
    factory { UserInteractor(get()) }
    viewModel { ProfileRegistrationViewModel(get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SocketSelectionViewModel() }
}