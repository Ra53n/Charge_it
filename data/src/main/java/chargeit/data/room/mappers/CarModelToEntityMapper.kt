package chargeit.data.room.mappers

import chargeit.data.domain.model.CarEntity
<<<<<<< HEAD
import chargeit.data.room.model.CarModel

class CarModelToEntityMapper(private val mapper: SocketModelToEntityMapper) {
=======
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.CarModel

class CarModelToEntityMapper {
>>>>>>> origin/feature_data

    fun map(carModel: CarModel): CarEntity {
        return CarEntity(
            id = carModel.id,
            brand = carModel.brand,
            model = carModel.model,
<<<<<<< HEAD
            listOfSockets = carModel.listOfSockets.map { mapper.map(it) },
=======
            listOfSockets = carModel.listOfSockets.map { Socket.valueOf(it.id) },
>>>>>>> origin/feature_data
        )
    }
}