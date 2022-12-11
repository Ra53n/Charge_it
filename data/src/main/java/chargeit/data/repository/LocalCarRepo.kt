package chargeit.data.repository

import chargeit.data.domain.model.CarEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface LocalCarRepo {

    fun getAllCar(): Flowable<List<CarEntity>>

    fun saveCarEntity(car: CarEntity): Completable
}