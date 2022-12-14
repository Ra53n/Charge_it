package chargeit.data.room.mappers

import chargeit.data.domain.model.SocketEntity
import chargeit.data.room.model.SocketModel

class SocketModelToEntityMapper {

    fun map(socketModel: SocketModel): SocketEntity {
        return with(socketModel) {
            SocketEntity(
                id,
                socket,
                status?: false
            )
        }
    }

    fun map(socketEntity: SocketEntity): SocketModel {
        return with(socketEntity) {
            SocketModel(
                id,
                socket,
                status?: false
            )
        }
    }
}