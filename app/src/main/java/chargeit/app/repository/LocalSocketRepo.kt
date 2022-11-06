package chargeit.app.repository

import chargeit.app.room.SocketEntity

interface LocalSocketRepo {

    fun getAllSocket(): List<SocketEntity>

    fun saveSocketEntity(socket: SocketEntity)
}