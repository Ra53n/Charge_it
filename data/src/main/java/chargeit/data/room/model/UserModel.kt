package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
<<<<<<< HEAD
    val id: String,
=======
    val id: Int,
>>>>>>> origin/feature_data
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val car: CarModel,
)