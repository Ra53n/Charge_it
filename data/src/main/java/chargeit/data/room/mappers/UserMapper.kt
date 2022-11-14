package chargeit.data.room.mappers

import chargeit.data.domain.model.UserEntity
import chargeit.data.room.model.UserModel

class UserMapper {

    fun toUserModel(user: UserEntity): UserModel {
        return UserModel(
            id = user.id,
            name = user.name,
            surname = user.surname,
            phoneNumber = user.phoneNumber,
            email = user.email,
            car = user.car,
        )
    }

    fun toUserEntity(userModel: UserModel): UserEntity {
        return UserEntity(
            id = userModel.id,
            name = userModel.name,
            surname = userModel.surname,
            phoneNumber = userModel.phoneNumber,
            email = userModel.email,
            car = userModel.car,
        )
    }
}