package chargeit.data.repository

import chargeit.data.room.dao.CarDao
import chargeit.data.room.model.CarModel

class LocalCarRepoImpl(private val carDao: CarDao) : LocalCarRepo {

    override fun getAllCar(): List<CarModel> = carDao.all()

    override fun saveCarEntity(car: CarModel) {
        carDao.getCarByBrand(car.brand)
    }
}