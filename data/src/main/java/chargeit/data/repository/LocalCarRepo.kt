package chargeit.data.repository

import chargeit.data.domain.model.CarEntity

interface LocalCarRepo {

    fun getAllCar(): List<CarEntity>

    fun saveCarEntity(car: CarEntity)
}