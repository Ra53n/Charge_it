package chargeit.app.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val brand: String,
    val model: String,
    val listOfChargers: List<ElectricStationEntity>,
)
