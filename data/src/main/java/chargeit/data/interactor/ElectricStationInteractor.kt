package chargeit.data.interactor

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.repository.LocalElectricStationRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class ElectricStationInteractor (private val electricStationRepo: LocalElectricStationRepo) {
    fun getElectricStationById(id: Int): Flowable<List<ElectricStationEntity>> {
        return electricStationRepo.getElectricStationById(id)
    }

    fun updateElectricStation(entity: ElectricStationEntity): Completable {
        return electricStationRepo.updateElectricStation(entity)
    }
}