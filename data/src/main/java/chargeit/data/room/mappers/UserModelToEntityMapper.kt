package chargeit.data.room.mappers

import chargeit.data.domain.model.UserEntity
import chargeit.data.room.model.UserModel

class UserModelToEntityMapper(private val mapper: CarModelToEntityMapper) {

    fun map(userModel: UserModel): UserEntity {
        return UserEntity(
            id = userModel.id,
            name = userModel.name,
            surname = userModel.surname,
            phoneNumber = userModel.phoneNumber,
            email = userModel.email,
            car = mapper.map(userModel.car),
        )
    }
}