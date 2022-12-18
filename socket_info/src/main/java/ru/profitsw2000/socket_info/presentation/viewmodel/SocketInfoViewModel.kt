package ru.profitsw2000.socket_info.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.State
import chargeit.data.interactor.ElectricStationInteractor
import chargeit.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SocketInfoViewModel(
    private val interactor: ElectricStationInteractor,
    private val navigator: Navigator
) : CoreViewModel() {

    private val _electricStationLiveData = MutableLiveData<List<ElectricStationEntity>>()
    val electricStationLiveData: LiveData<List<ElectricStationEntity>> by this::_electricStationLiveData

    private val _updateLiveData = MutableLiveData<State>()
    val updateLiveData: LiveData<State> by this::_updateLiveData

    fun getElectricStationInfo(id: Int) {
        interactor.getElectricStationById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _electricStationLiveData.value = it }
    }

    fun updateElectricStation(electricStationEntity: ElectricStationEntity) {
        interactor.updateElectricStation(electricStationEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _updateLiveData.value = State.Success
                },
                {
                    _updateLiveData.value = State.Error
                }
            )
    }

    fun navigateUp() {
        navigator.navigateUp()
    }
}