package chargeit.profilescreen.di

import chargeit.data.interactor.CarInteractor
import chargeit.data.interactor.UserInteractor
import chargeit.data.repository.LocalCarRepo
import chargeit.profilescreen.data.mapper.UserMapper
import chargeit.profilescreen.data.repository.FakeCarRepoImpl
import chargeit.profilescreen.viewmodel.LoginViewModel
import chargeit.profilescreen.viewmodel.ProfileRegistrationViewModel
import chargeit.profilescreen.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileScreenModule = module {
    single<LocalCarRepo> { FakeCarRepoImpl() }
    factory { CarInteractor(get()) }
    factory { UserMapper() }
    factory { UserInteractor(get()) }
    viewModel { ProfileRegistrationViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}