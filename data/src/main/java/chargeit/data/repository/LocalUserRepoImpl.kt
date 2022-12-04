package chargeit.data.repository

import chargeit.data.domain.model.UserEntity
import chargeit.data.room.database.AppDatabase
import chargeit.data.room.mappers.UserModelToEntityMapper
import io.reactivex.rxjava3.core.Flowable

class LocalUserRepoImpl(
    private val database: AppDatabase,
    private val mapper: UserModelToEntityMapper
) : LocalUserRepo {

    override fun getAllUser(): Flowable<List<UserEntity>> =
        database.userDao.all()
            .map { list -> list.map { mapper.map(it) } }

    override fun getUserByPhone(phone: String): Flowable<List<UserEntity>> =
        database.userDao.getUserByPhone(phone)
            .map { list -> list.map { mapper.map(it) } }


    override fun saveUserEntity(user: UserEntity) =
        database.userDao.insert(mapper.map(user))

}