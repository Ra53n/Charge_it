package chargeit.data.domain.model

data class UserEntity(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val car: CarEntity,
)