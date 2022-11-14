package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SocketModel(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
)