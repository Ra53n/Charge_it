package chargeit.data.repository

<<<<<<< HEAD
import chargeit.data.room.dao.SocketDao
import chargeit.data.room.model.SocketModel

class LocalSocketRepoImpl(private val socketDao: SocketDao) : LocalSocketRepo {

    override fun getAllSocket(): List<SocketModel> = socketDao.all()

    override fun saveSocketEntity(socket: SocketModel) {
        socketDao.getSocketByTitle(socket.title)
    }
=======
import chargeit.data.domain.model.Socket

class LocalSocketRepoImpl : LocalSocketRepo {

    override fun getAllSocket(): List<Socket> = Socket.getAllSockets()
>>>>>>> origin/feature_data
}