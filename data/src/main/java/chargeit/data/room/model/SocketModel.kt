package chargeit.data.room.model

import androidx.room.Entity
import chargeit.data.domain.model.Socket
import java.util.UUID

@Entity
data class SocketModel(
    val id: UUID = UUID.randomUUID(),
    val socket: Socket,
    val status: Boolean? = true,
)