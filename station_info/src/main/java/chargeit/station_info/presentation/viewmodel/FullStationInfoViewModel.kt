package chargeit.station_info.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.interactor.ElectricStationInteractor
import chargeit.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FullStationInfoViewModel(
    private val interactor: ElectricStationInteractor,
    private val navigator: Navigator
) : CoreViewModel() {

    private val _electricStationLiveData = MutableLiveData<List<ElectricStationEntity>>()
    val electricStationLiveData: LiveData<List<ElectricStationEntity>> by this::_electricStationLiveData

    fun navigateToSocketInfoScreen(bundle: Bundle) {
        navigator.navigateToSocketInfo(bundle)
    }

    fun getElectricStationInfo(id: Int) {
        interactor.getElectricStationById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _electricStationLiveData.value = it }
    }

}