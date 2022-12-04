package chargeit.data.room.mappers

import chargeit.data.domain.model.UserEntity
import chargeit.data.room.model.UserModel

class UserModelToEntityMapper(private val mapper: CarModelToEntityMapper) {

    fun map(userModel: UserModel): UserEntity {
        return UserEntity(
            name = userModel.name,
            phoneNumber = userModel.phoneNumber,
            email = userModel.email,
            car = mapper.map(userModel.car),
        )
    }

    fun map(userModel: UserEntity): UserModel {
        return UserModel(
            name = userModel.name,
            phoneNumber = userModel.phoneNumber,
            email = userModel.email,
            car = mapper.map(userModel.car),
        )
    }
}