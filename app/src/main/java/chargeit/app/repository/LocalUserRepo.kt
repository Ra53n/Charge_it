package chargeit.app.repository

import chargeit.app.room.UserEntity

interface LocalUserRepo {

    fun getAllUser(): List<UserEntity>

    fun saveUserEntity(user: UserEntity)
}