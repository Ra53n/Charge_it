package chargeit.app.repository

import chargeit.app.room.entities.SocketEntity

interface LocalSocketRepo {

    fun getAllSocket(): List<SocketEntity>

    fun saveSocketEntity(socket: SocketEntity)
}