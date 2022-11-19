package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import chargeit.data.domain.model.Socket

@Entity
data class ElectricStationModel(
    @PrimaryKey(autoGenerate = true)
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
)