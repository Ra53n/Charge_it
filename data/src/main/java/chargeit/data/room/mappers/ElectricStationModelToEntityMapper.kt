package chargeit.data.room.mappers

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.room.model.ElectricStationModel

class ElectricStationModelToEntityMapper(private val mapper: SocketModelToEntityMapper) {

    fun map(electricStationModel: ElectricStationModel): ElectricStationEntity {
        return ElectricStationEntity(
            id = electricStationModel.id,
            lat = electricStationModel.lat,
            lon = electricStationModel.lon,
            description = electricStationModel.description,
            listOfSockets = electricStationModel.listOfSockets.map { mapper.map(it) },
            status = electricStationModel.status,
        )
    }
}