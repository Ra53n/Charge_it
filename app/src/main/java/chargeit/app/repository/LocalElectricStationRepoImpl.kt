package chargeit.app.repository

import chargeit.app.room.dao.ElectricStationDao
import chargeit.app.room.entities.ElectricStationEntity

class LocalElectricStationRepoImpl(private val electricStationDao: ElectricStationDao) :
    LocalElectricStationRepo {

    override fun getAllElectricStation(): List<ElectricStationEntity> = electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationEntity) {
        electricStationDao.getElectricStationById(electricStation.id)
    }
}