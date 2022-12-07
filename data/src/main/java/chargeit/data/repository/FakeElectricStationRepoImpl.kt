package chargeit.data.repository

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

}

