package chargeit.app.repository

import chargeit.app.room.CarEntity
import chargeit.app.room.ElectricStationEntity
import chargeit.app.room.SocketEntity
import chargeit.app.room.UserEntity

interface LocalRepository {

    fun getAllCar(): List<CarEntity>
    fun getAllElectricStation(): List<ElectricStationEntity>
    fun getAllSocket(): List<SocketEntity>
    fun getAllUser(): List<UserEntity>

    fun saveCarEntity(car: CarEntity)
    fun saveElectricStationEntity(electricStation: ElectricStationEntity)
    fun saveSocketEntity(socket: SocketEntity)
    fun saveUserEntity(user: UserEntity)
}