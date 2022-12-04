package chargeit.data.interactor

import chargeit.data.repository.LocalCarRepo

class CarInteractor(
    private val carRepo: LocalCarRepo
) {

    fun getAllCarBrands() = carRepo.getAllCar().map { it.brand }

    fun getAllModelsByBrand(brand: String) =
        carRepo.getAllCar()
            .filter { it.brand == brand }
            .map { it.model }
}