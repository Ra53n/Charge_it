package chargeit.profilescreen.data.repository

import chargeit.data.domain.model.CarEntity
import chargeit.data.domain.model.Socket
import chargeit.data.repository.LocalCarRepo

class FakeCarRepoImpl : LocalCarRepo {
    override fun getAllCar(): List<CarEntity> {
        return listOf(
            CarEntity(
                id = 1,
                brand = "Tesla",
                model = "Model S",
                listOfSockets = Socket.getAllSockets()
            ),
            CarEntity(
                id = 2,
                brand = "Audi",
                model = "E-tron",
                listOfSockets = Socket.getAllSockets()
            ),
            CarEntity(
                id = 3,
                brand = "Mercedes",
                model = "EQS",
                listOfSockets = Socket.getAllSockets()
            ),
            CarEntity(
                id = 4,
                brand = "BMW",
                model = "i3",
                listOfSockets = Socket.getAllSockets()
            ),
            CarEntity(
                id = 5,
                brand = "Nissan",
                model = "Leaf",
                listOfSockets = Socket.getAllSockets()
            )
        )
    }

    override fun saveCarEntity(car: CarEntity) {
        TODO("Not yet implemented")
    }
}