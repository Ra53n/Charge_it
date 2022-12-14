package chargeit.data.room.mappers

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.model.ElectricStationModel

class ElectricStationModelToEntityMapper(private val socketMapper: SocketModelToEntityMapper) {

    fun map(electricStationModel: ElectricStationModel): ElectricStationEntity {
        return ElectricStationEntity(
            id = electricStationModel.id,
            lat = electricStationModel.lat,
            lon = electricStationModel.lon,
            description = electricStationModel.description,
            listOfSockets = electricStationModel.listOfSockets.map { socketMapper.map(it) },
            status = electricStationModel.status,
            titleStation = electricStationModel.titleStation,
            workTime = electricStationModel.workTime,
            additionalInfo = electricStationModel.additionalInfo.ifEmpty { "Нет дополнительной информации" },
            paidCost = electricStationModel.paidCost,
            freeCost = electricStationModel.freeCost
        )
    }


    fun map(electricStationEntity: ElectricStationEntity): ElectricStationModel {
        return ElectricStationModel(
            id = electricStationEntity.id,
            lat = electricStationEntity.lat,
            lon = electricStationEntity.lon,
            description = electricStationEntity.description,
            listOfSockets = electricStationEntity.listOfSockets.map { socketMapper.map(it) },
            status = electricStationEntity.status,
            titleStation = electricStationEntity.titleStation,
            workTime = electricStationEntity.workTime,
            additionalInfo = electricStationEntity.additionalInfo.ifEmpty { "Нет дополнительной информации" },
            paidCost = electricStationEntity.paidCost,
            freeCost = electricStationEntity.freeCost
        )
    }
}