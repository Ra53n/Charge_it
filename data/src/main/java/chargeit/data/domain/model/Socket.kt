package chargeit.data.domain.model

class Socket(
    val id: Int,
    val icon: Int,
    val title: String,
    val description: String,
) {
    companion object {
        // В это поле нужно вносить все виды разъемов
        private val socketList = listOf<Socket>(
            Socket(1, 1, "", ""),
            Socket(2, 2, "", "")
        )

        val EMPTY_SOCKET = Socket(0, 0, "", "")

        fun getAllSockets() = socketList

        fun valueOf(id: Int) = socketList.find { it.id == id } ?: EMPTY_SOCKET
    }
}




