package chargeit.data.repository

import chargeit.data.room.dao.SocketDao
import chargeit.data.room.model.SocketModel

class LocalSocketRepoImpl(private val socketDao: SocketDao) : LocalSocketRepo {

    override fun getAllSocket(): List<SocketModel> = socketDao.all()

    override fun saveSocketEntity(socket: SocketModel) {
        socketDao.getSocketByTitle(socket.title)
    }
}