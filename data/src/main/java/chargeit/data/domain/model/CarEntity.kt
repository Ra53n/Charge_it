package chargeit.data.domain.model

data class CarEntity(
    val id: Int,
    val brand: String,
    val model: String,
    val listOfSockets: List<Socket>,
)
