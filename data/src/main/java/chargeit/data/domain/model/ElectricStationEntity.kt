package chargeit.data.domain.model

data class ElectricStationEntity(
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
    val freeCost: Boolean,
)