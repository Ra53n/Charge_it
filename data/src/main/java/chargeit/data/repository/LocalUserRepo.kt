package chargeit.data.repository

import chargeit.data.domain.model.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface LocalUserRepo {

    fun getAllUser(): Flowable<List<UserEntity>>

    fun getUserByPhone(phone: String): Flowable<List<UserEntity>>

    fun saveUserEntity(user: UserEntity): Completable
}