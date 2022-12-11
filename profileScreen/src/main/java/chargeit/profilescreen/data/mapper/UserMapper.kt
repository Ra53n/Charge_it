package chargeit.profilescreen.data.mapper

import chargeit.data.domain.model.CarEntity
import chargeit.data.domain.model.Socket
import chargeit.data.domain.model.UserEntity
import chargeit.profilescreen.data.model.UserUiModel
import java.util.*

class UserMapper {

    fun map(uiModel: UserUiModel, socketList: List<Socket>): UserEntity {
        return with(uiModel) {
            UserEntity(
                phoneNumber = phone,
                name = name,
                email = email,
                car = CarEntity(
                    Random().nextInt(),
                    brand.ifEmpty { NO_SUCH_FIELD },
                    model.ifEmpty { NO_SUCH_FIELD },
                    emptyList()
                ),
                sockets = socketList
            )
        }
    }

    fun map(entity: UserEntity): UserUiModel {
        return with(entity) {
            UserUiModel(
                name = name,
                phone = phoneNumber,
                email = email,
                brand = car.brand,
                model = car.model,
                socket = mapSocketListToString(sockets)
            )
        }
    }

    fun mapSocketListToString(list: List<Socket>): String {
        val resultString = StringBuilder()
        for (index in list.indices) {
            if (index != list.lastIndex) {
                resultString.append(list[index].title + ", ")
            } else {
                resultString.append(list[index].title)
            }
        }
        return resultString.toString()
    }

    companion object {
        const val NO_SUCH_FIELD = "Параметр не указан"
    }
}