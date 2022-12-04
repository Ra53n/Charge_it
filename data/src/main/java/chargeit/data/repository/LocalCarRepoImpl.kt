package chargeit.data.repository

import chargeit.data.domain.model.CarEntity
import chargeit.data.room.database.AppDatabase

class LocalCarRepoImpl(private val database: AppDatabase) : LocalCarRepo {

    override fun getAllCar(): List<CarEntity> =
        database.carDao.all().map { CarEntity(it.id, it.brand, it.model, it.listOfSockets) }

    override fun saveCarEntity(car: CarEntity) {
        database.carDao.getCarByBrand(car.brand)
    }
}