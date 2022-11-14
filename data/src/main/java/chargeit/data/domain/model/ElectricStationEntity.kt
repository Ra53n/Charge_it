package chargeit.data.domain.model

import chargeit.data.room.model.SocketModel

data class ElectricStationEntity(
    val id: String,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<SocketModel>,
    val status: String,
)