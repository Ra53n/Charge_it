package chargeit.data.room.mappers

import chargeit.data.domain.model.SocketEntity
import chargeit.data.room.model.SocketModel

class SocketMapper {

    fun toSocketModel(socket: SocketEntity): SocketModel {
        return SocketModel(
            id = socket.id,
            icon = socket.icon,
            title = socket.title,
            description = socket.description,
        )
    }

    fun toSocketEntity(socketModel: SocketModel): SocketEntity {
        return SocketEntity(
            id = socketModel.id,
            icon = socketModel.icon,
            title = socketModel.title,
            description = socketModel.description,
        )
    }
}