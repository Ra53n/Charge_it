package chargeit.data.room.mappers

import chargeit.data.domain.model.CarEntity
import chargeit.data.room.model.CarModel

class CarModelToEntityMapper(private val mapper: SocketModelToEntityMapper) {

    fun map(carModel: CarModel): CarEntity {
        return CarEntity(
            id = carModel.id,
            brand = carModel.brand,
            model = carModel.model,
            listOfSockets = carModel.listOfSockets.map { mapper.map(it) },
        )
    }
}