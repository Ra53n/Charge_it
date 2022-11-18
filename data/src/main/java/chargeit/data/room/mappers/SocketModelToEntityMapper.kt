package chargeit.data.room.mappers

import chargeit.data.domain.model.SocketEntity
import chargeit.data.room.model.SocketModel

class SocketModelToEntityMapper() {

    fun map(socketModel: SocketModel): SocketEntity {
        return SocketEntity(
            id = socketModel.id,
            icon = socketModel.icon,
            title = socketModel.title,
            description = socketModel.description,
        )
    }
}