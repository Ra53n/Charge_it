package chargeit.data.repository

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.model.ElectricStationModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface LocalElectricStationRepo {

    fun getAllElectricStation(): Flowable<List<ElectricStationModel>>

    fun saveElectricStationEntity(electricStation: ElectricStationModel): Completable

    fun getElectricStationById(id: Int): Flowable<List<ElectricStationEntity>>

    fun updateElectricStation(entity: ElectricStationEntity): Completable
}