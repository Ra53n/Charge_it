package chargeit.app.di

import android.content.Context
import chargeit.app.navigation.NavigatorImpl
import chargeit.data.repository.LocalUserRepo
import chargeit.data.repository.LocalUserRepoImpl
import chargeit.data.room.database.AppDatabase
import chargeit.data.room.mappers.CarModelToEntityMapper
import chargeit.data.room.mappers.UserModelToEntityMapper
import chargeit.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        AppDatabase.create(androidContext())
        AppDatabase.getInstance()
    }
    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }
    factory { CarModelToEntityMapper() }
    factory { UserModelToEntityMapper(get()) }
    single<LocalUserRepo> { LocalUserRepoImpl(get(), get()) }
    single<Navigator> { NavigatorImpl() }
}

private const val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"