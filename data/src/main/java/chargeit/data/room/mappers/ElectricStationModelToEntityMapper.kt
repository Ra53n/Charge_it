package chargeit.data.room.mappers

import chargeit.data.domain.model.ElectricStationEntity
<<<<<<< HEAD
import chargeit.data.room.model.ElectricStationModel

class ElectricStationModelToEntityMapper(private val mapper: SocketModelToEntityMapper) {
=======
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.ElectricStationModel

class ElectricStationModelToEntityMapper() {
>>>>>>> origin/feature_data

    fun map(electricStationModel: ElectricStationModel): ElectricStationEntity {
        return ElectricStationEntity(
            id = electricStationModel.id,
            lat = electricStationModel.lat,
            lon = electricStationModel.lon,
            description = electricStationModel.description,
<<<<<<< HEAD
            listOfSockets = electricStationModel.listOfSockets.map { mapper.map(it) },
            status = electricStationModel.status,
            titleStation = "",
            workTime = "",
            additionalInfo = "",
            paidCost = false,
            freeCost = false
=======
            listOfSockets = electricStationModel.listOfSockets.map { Socket.valueOf(it.id) },
            status = electricStationModel.status,
            titleStation = electricStationModel.titleStation,
            workTime = electricStationModel.workTime,
            additionalInfo = electricStationModel.additionalInfo,
            paidCost = electricStationModel.paidCost,
            freeCost = electricStationModel.freeCost
>>>>>>> origin/feature_data
        )
    }
}