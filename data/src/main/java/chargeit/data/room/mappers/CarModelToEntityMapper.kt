package chargeit.data.room.mappers

import chargeit.data.domain.model.CarEntity
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.CarModel

class CarModelToEntityMapper {

    fun map(carModel: CarModel): CarEntity {
        return CarEntity(
            id = carModel.id,
            brand = carModel.brand,
            model = carModel.model,
            listOfSockets = carModel.listOfSockets.map { Socket.valueOf(it.id) },
        )
    }

    fun map(carModel: CarEntity): CarModel {
        return CarModel(
            id = carModel.id,
            brand = carModel.brand,
            model = carModel.model,
            listOfSockets = carModel.listOfSockets.map { Socket.valueOf(it.id) },
        )
    }
}