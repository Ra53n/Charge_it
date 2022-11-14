package chargeit.data.room.mappers

import chargeit.data.domain.model.CarEntity
import chargeit.data.room.model.CarModel

class CarMapper {

    fun toCarModel(car: CarEntity): CarModel {
        return CarModel(
            id = car.id,
            brand = car.brand,
            model = car.model,
            listOfSockets = car.listOfSockets,
        )
    }

    fun toCarEntity(carModel: CarModel): CarEntity {
        return CarEntity(
            id = carModel.id,
            brand = carModel.brand,
            model = carModel.model,
            listOfSockets = carModel.listOfSockets,
        )
    }
}