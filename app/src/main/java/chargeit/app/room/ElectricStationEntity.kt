package chargeit.app.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ElectricStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val coordinates: String,
    val description: String,
    val listOfSockets: List<SocketEntity>,
    val status: String,
)
