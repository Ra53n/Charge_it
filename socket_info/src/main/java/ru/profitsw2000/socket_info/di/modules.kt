package ru.profitsw2000.socket_info.di

import org.koin.dsl.module
import ru.profitsw2000.socket_info.presentation.viewmodel.SocketInfoViewModel


val socketModule = module {
    single { SocketInfoViewModel(get()) }
}