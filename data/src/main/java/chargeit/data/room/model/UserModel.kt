package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val car: CarModel,
)