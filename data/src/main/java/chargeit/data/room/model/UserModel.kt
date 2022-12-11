package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import chargeit.data.domain.model.Socket

@Entity
data class UserModel(
    @PrimaryKey
    val phoneNumber: Long,
    val name: String,
    val email: String,
    val car: CarModel,
    val sockets: List<Socket>,
)