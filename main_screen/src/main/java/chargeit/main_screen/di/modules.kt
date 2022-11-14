package chargeit.main_screen.di

import androidx.lifecycle.MutableLiveData
import chargeit.main_screen.domain.DatasetState
import chargeit.main_screen.domain.LocationState
import chargeit.main_screen.ui.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainScreenModule = module {

    single<MutableLiveData<DatasetState>>(named("DatasetState")) { MutableLiveData() }
    single<MutableLiveData<LocationState>>(named("LocationState")) { MutableLiveData() }

    viewModel {
        MapsFragmentViewModel(
            get(),
            get(named("LocationState")),
            get(named("DatasetState"))
        )
    }

}