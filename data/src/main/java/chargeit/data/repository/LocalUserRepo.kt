package chargeit.data.repository

import chargeit.data.room.model.UserModel

interface LocalUserRepo {

    fun getAllUser(): List<UserModel>

    fun saveUserEntity(user: UserModel)
}