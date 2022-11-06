package chargeit.app.repository

import chargeit.app.room.ElectricStationEntity

interface LocalElectricStationRepo {

    fun getAllElectricStation(): List<ElectricStationEntity>

    fun saveElectricStationEntity(electricStation: ElectricStationEntity)
}