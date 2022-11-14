package chargeit.data.repository

import chargeit.data.room.dao.UserDao
import chargeit.data.room.model.UserModel

class LocalUserRepoImpl(private val userDao: UserDao) : LocalUserRepo {

    override fun getAllUser(): List<UserModel> = userDao.all()

    override fun saveUserEntity(user: UserModel) {
        userDao.getUserByPhone(user.phoneNumber)
    }
}