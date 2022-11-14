package chargeit.data.domain.model

import chargeit.data.room.model.CarModel

data class UserEntity(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val car: CarModel,
)