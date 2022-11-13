package chargeit.app.repository

import chargeit.app.room.entities.ElectricStationEntity

interface LocalElectricStationRepo {

    fun getAllElectricStation(): List<ElectricStationEntity>

    fun saveElectricStationEntity(electricStation: ElectricStationEntity)
}