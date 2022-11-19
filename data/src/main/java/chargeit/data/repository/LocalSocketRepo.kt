package chargeit.data.repository

<<<<<<< HEAD
import chargeit.data.room.model.SocketModel

interface LocalSocketRepo {

    fun getAllSocket(): List<SocketModel>

    fun saveSocketEntity(socket: SocketModel)
=======
import chargeit.data.domain.model.Socket

interface LocalSocketRepo {

    fun getAllSocket(): List<Socket>
>>>>>>> origin/feature_data
}