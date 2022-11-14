package chargeit.data.room.mappers

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.model.ElectricStationModel

class ElectricStationMapper {

    fun toElectricStationModel(electricStation: ElectricStationEntity): ElectricStationModel {
        return ElectricStationModel(
            id = electricStation.id,
            lat = electricStation.lat,
            lon = electricStation.lon,
            description = electricStation.description,
            listOfSockets = electricStation.listOfSockets,
            status = electricStation.status,
        )
    }

    fun toElectricStationEntity(electricStationModel: ElectricStationModel): ElectricStationEntity {
        return ElectricStationEntity(
            id = electricStationModel.id,
            lat = electricStationModel.lat,
            lon = electricStationModel.lon,
            description = electricStationModel.description,
            listOfSockets = electricStationModel.listOfSockets,
            status = electricStationModel.status,
        )
    }
}