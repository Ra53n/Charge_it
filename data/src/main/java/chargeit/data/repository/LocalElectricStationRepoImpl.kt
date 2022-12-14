package chargeit.data.repository

import chargeit.data.room.database.AppDatabase
import chargeit.data.room.model.ElectricStationModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class LocalElectricStationRepoImpl(private val database: AppDatabase) : LocalElectricStationRepo {

    override fun getAllElectricStation() =
        database.electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationModel) =
        database.electricStationDao.insert(electricStation)

    override fun getElectricStationById(id: Int): Flowable<List<ElectricStationModel>> =
        database.electricStationDao.getElectricStationById(id)

    override fun updateElectricStation(model: ElectricStationModel): Completable =
        database.electricStationDao.update(model)
}