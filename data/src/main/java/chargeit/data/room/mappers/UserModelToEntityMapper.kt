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
            sockets = userModel.sockets
        )
    }

    fun map(userEntity: UserEntity): UserModel {
        return UserModel(
            name = userEntity.name,
            phoneNumber = userEntity.phoneNumber,
            email = userEntity.email,
            car = mapper.map(userEntity.car),
            sockets = userEntity.sockets
        )
    }
}