package chargeit.data.domain.model

data class UserEntity(
    val phoneNumber: Long,
    val name: String,
    val email: String,
    val car: CarEntity,
    val sockets: List<Socket>,
)