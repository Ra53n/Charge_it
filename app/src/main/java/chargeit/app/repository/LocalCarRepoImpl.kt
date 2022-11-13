package chargeit.app.repository

import chargeit.app.room.dao.CarDao
import chargeit.app.room.entities.CarEntity

class LocalCarRepoImpl(private val carDao: CarDao) : LocalCarRepo {

    override fun getAllCar(): List<CarEntity> = carDao.all()

    override fun saveCarEntity(car: CarEntity) {
        carDao.getCarByBrand(car.brand)
    }
}