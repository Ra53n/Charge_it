package chargeit.data.repository

import chargeit.data.room.dao.ElectricStationDao
import chargeit.data.room.model.ElectricStationModel

class LocalElectricStationRepoImpl(private val electricStationDao: ElectricStationDao) :
    LocalElectricStationRepo {

    override fun getAllElectricStation(): List<ElectricStationModel> = electricStationDao.all()

    override fun saveElectricStationEntity(electricStation: ElectricStationModel) {
        electricStationDao.getElectricStationById(electricStation.id)
    }
}