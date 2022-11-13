package chargeit.app.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SocketEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
)
