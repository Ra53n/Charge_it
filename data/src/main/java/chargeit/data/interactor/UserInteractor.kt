package chargeit.data.interactor

import chargeit.data.domain.model.UserEntity
import chargeit.data.repository.LocalUserRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class UserInteractor(
    private val userRepo: LocalUserRepo
) {
    fun getAllUser(): Flowable<List<UserEntity>> {
        return userRepo.getAllUser()
    }

    fun saveUserEntity(user: UserEntity): Completable {
        return userRepo.saveUserEntity(user)
    }

    fun loginUser(name: String, phone: String): Flowable<UserEntity> {
        return userRepo.getUserByPhone(phone)
            .flatMap { userList ->
                val user = userList.firstOrNull { it.name == name }
                if (user != null) {
                    Flowable.just(user)
                } else {
                    Flowable.error(Throwable("No such user"))
                }
            }
    }
}