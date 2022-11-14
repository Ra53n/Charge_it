package chargeit.data.domain.model

import chargeit.data.room.model.SocketModel

data class CarEntity(
    val id: Int,
    val brand: String,
    val model: String,
    val listOfSockets: List<SocketModel>,
)
