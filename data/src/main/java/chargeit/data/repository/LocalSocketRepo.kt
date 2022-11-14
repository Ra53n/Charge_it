package chargeit.data.repository

import chargeit.data.room.model.SocketModel

interface LocalSocketRepo {

    fun getAllSocket(): List<SocketModel>

    fun saveSocketEntity(socket: SocketModel)
}