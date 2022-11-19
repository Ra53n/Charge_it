package chargeit.data.domain.model

data class ElectricStationEntity(
<<<<<<< HEAD
    val id: String,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<SocketEntity>,
=======
    val id: Int,
    val lat: Double,
    val lon: Double,
    val description: String,
    val listOfSockets: List<Socket>,
>>>>>>> origin/feature_data
    val status: String,
    val titleStation: String,
    val workTime: String,
    val additionalInfo: String,
    val paidCost: Boolean,
    val freeCost: Boolean,
)