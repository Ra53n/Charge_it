package chargeit.app.di

import chargeit.app.navigation.NavigatorImpl
import chargeit.navigator.Navigator
import org.koin.dsl.module

val navModule = module {
    single<Navigator> { NavigatorImpl() }
}