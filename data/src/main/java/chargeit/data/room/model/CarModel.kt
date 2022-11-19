package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
<<<<<<< HEAD
=======
import chargeit.data.domain.model.Socket
>>>>>>> origin/feature_data

@Entity
data class CarModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val brand: String,
    val model: String,
<<<<<<< HEAD
    val listOfSockets: List<SocketModel>,
=======
    val listOfSockets: List<Socket>,
>>>>>>> origin/feature_data
)