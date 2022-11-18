package chargeit.data.domain.model

data class ElectricStationEntity(
    val id: String,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<SocketEntity>,
    val status: String,
)