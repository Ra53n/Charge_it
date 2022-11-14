package chargeit.data.repository

import chargeit.data.room.model.ElectricStationModel

interface LocalElectricStationRepo {

    fun getAllElectricStation(): List<ElectricStationModel>

    fun saveElectricStationEntity(electricStation: ElectricStationModel)
}