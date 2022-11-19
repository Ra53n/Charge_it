package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import chargeit.data.domain.model.Socket

@Entity
data class CarModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val brand: String,
    val model: String,
    val listOfSockets: List<Socket>,
)