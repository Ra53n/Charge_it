package chargeit.data.repository

import chargeit.data.domain.model.CarEntity
import chargeit.data.room.database.AppDatabase
import chargeit.data.room.mappers.CarModelToEntityMapper
import io.reactivex.rxjava3.core.Flowable

class LocalCarRepoImpl(
    private val database: AppDatabase,
    private val mapper: CarModelToEntityMapper
) : LocalCarRepo {

    override fun getAllCar(): Flowable<List<CarEntity>> =
        database.carDao.all()
            .map { list -> list.map { mapper.map(it) } }

    override fun saveCarEntity(car: CarEntity) =
        database.carDao.insert(mapper.map(car))
}