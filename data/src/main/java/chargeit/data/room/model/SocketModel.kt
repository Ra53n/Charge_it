package chargeit.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import chargeit.data.domain.model.Socket

@Entity
data class SocketModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val socket: Socket,
    val status: Boolean? = false,
)