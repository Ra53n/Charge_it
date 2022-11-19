package chargeit.data.room.mappers

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.ElectricStationModel

class ElectricStationModelToEntityMapper() {

    fun map(electricStationModel: ElectricStationModel): ElectricStationEntity {
        return ElectricStationEntity(
            id = electricStationModel.id,
            lat = electricStationModel.lat,
            lon = electricStationModel.lon,
            description = electricStationModel.description,
            listOfSockets = electricStationModel.listOfSockets.map { Socket.valueOf(it.id) },
            status = electricStationModel.status,
            titleStation = electricStationModel.titleStation,
            workTime = electricStationModel.workTime,
            additionalInfo = electricStationModel.additionalInfo,
            paidCost = electricStationModel.paidCost,
            freeCost = electricStationModel.freeCost
        )
    }
}