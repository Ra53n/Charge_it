package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
<<<<<<< HEAD
=======
import chargeit.data.domain.model.Socket
>>>>>>> origin/feature_data

@Entity
data class ElectricStationModel(
    @PrimaryKey(autoGenerate = true)
<<<<<<< HEAD
    val id: String,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<SocketModel>,
    val status: String,
=======
    val id: Int,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<Socket>,
    val status: String,
    val titleStation: String,
    val workTime: String,
    val additionalInfo: String,
    val paidCost: Boolean,
    val freeCost: Boolean
>>>>>>> origin/feature_data
)