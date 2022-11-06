package chargeit.app.repository

import chargeit.app.room.SocketDao
import chargeit.app.room.SocketEntity

class LocalSocketRepoImpl(private val socketDao: SocketDao) : LocalSocketRepo {

    override fun getAllSocket(): List<SocketEntity> = socketDao.all()

    override fun saveSocketEntity(socket: SocketEntity) {
        socketDao.getSocketByTitle(socket.title)
    }
}