package chargeit.data.repository

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.database.AppDatabase
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.model.ElectricStationModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class LocalElectricStationRepoImpl(private val database: AppDatabase,
                                    private val mapper: ElectricStationModelToEntityMapper
                                   ) : LocalElectricStationRepo {

    override fun getAllElectricStation() =
        database.electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationModel) =
        database.electricStationDao.insert(electricStation)

    override fun getElectricStationById(id: Int): Flowable<List<ElectricStationEntity>> =
        database.electricStationDao.getElectricStationById(id)
            .map { list -> list.map { mapper.map(it) } }

    override fun updateElectricStation(entity: ElectricStationEntity) : Completable =
        database.electricStationDao.update(mapper.map(entity))
}