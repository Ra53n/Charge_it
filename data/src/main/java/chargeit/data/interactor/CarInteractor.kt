package chargeit.data.interactor

import chargeit.data.repository.LocalCarRepo
import io.reactivex.rxjava3.core.Flowable

class CarInteractor(
    private val carRepo: LocalCarRepo
) {

    fun getAllCarBrands(): Flowable<List<String>> =
        carRepo.getAllCar().map { list -> list.map { it.brand } }

    fun getAllModelsByBrand(brand: String): Flowable<List<String>> =
        carRepo.getAllCar()
            .map { list -> list.filter { it.brand == brand }.map { it.model } }
}