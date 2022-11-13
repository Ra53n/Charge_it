package chargeit.app.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ElectricStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<SocketEntity>,
    val status: String,
)
