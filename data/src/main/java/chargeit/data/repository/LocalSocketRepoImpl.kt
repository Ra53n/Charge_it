package chargeit.data.repository

import chargeit.data.domain.model.Socket

class LocalSocketRepoImpl : LocalSocketRepo {

    override fun getAllSocket(): List<Socket> = Socket.getAllSockets()
}