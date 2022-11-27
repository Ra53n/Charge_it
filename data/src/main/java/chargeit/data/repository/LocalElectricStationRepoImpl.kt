package chargeit.data.repository

import chargeit.data.room.dao.ElectricStationDao
import chargeit.data.room.database.AppDatabase
import chargeit.data.room.model.ElectricStationModel

class LocalElectricStationRepoImpl(private val database: AppDatabase) :
    LocalElectricStationRepo {

    override fun getAllElectricStation(): List<ElectricStationModel> = database.electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationModel) {
        database.electricStationDao.getElectricStationById(electricStation.id)
    }
}