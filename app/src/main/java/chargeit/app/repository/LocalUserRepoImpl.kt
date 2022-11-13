package chargeit.app.repository

import chargeit.app.room.dao.UserDao
import chargeit.app.room.entities.UserEntity

class LocalUserRepoImpl(private val userDao: UserDao) : LocalUserRepo {

    override fun getAllUser(): List<UserEntity> = userDao.all()

    override fun saveUserEntity(user: UserEntity) {
        userDao.getUserByPhone(user.phoneNumber)
    }
}