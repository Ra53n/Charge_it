package chargeit.data.repository

import chargeit.data.room.database.AppDatabase
import chargeit.data.room.model.ElectricStationModel

class LocalElectricStationRepoImpl(private val database: AppDatabase) : LocalElectricStationRepo {

    override fun getAllElectricStation() =
        database.electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationModel) =
        database.electricStationDao.insert(electricStation)
}