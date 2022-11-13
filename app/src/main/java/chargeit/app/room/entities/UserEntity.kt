package chargeit.app.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val car: CarEntity,
)
