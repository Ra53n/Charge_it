package chargeit.app.repository

import chargeit.app.room.entities.CarEntity

interface LocalCarRepo {

    fun getAllCar(): List<CarEntity>

    fun saveCarEntity(car: CarEntity)
}