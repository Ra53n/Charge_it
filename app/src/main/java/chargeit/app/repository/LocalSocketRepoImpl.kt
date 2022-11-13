package chargeit.app.repository

import chargeit.app.room.dao.SocketDao
import chargeit.app.room.entities.SocketEntity

class LocalSocketRepoImpl(private val socketDao: SocketDao) : LocalSocketRepo {

    override fun getAllSocket(): List<SocketEntity> = socketDao.all()

    override fun saveSocketEntity(socket: SocketEntity) {
        socketDao.getSocketByTitle(socket.title)
    }
}