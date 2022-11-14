package chargeit.data.repository

import chargeit.data.room.model.CarModel

interface LocalCarRepo {

    fun getAllCar(): List<CarModel>

    fun saveCarEntity(car: CarModel)
}