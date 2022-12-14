package chargeit.data.repository

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.baseElectricStationsList
import chargeit.data.room.model.ElectricStationModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


class FakeElectricStationRepoImpl : LocalElectricStationRepo {

    override fun getAllElectricStation(): Flowable<List<ElectricStationModel>> {
        return Flowable.just(baseElectricStationsList)
    }

    override fun saveElectricStationEntity(electricStation: ElectricStationModel): Completable {
        return Completable.complete()
    }

    override fun getElectricStationById(id: Int): Flowable<List<ElectricStationEntity>> {
        TODO("Not yet implemented")
    }

    override fun updateElectricStation(entity: ElectricStationEntity): Completable {
        TODO("Not yet implemented")
    }

}

