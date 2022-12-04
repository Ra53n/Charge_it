package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.UserModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM UserModel")
    fun all(): Flowable<List<UserModel>>

    @Query("SELECT * FROM UserModel WHERE phoneNumber LIKE :phoneNumber")
    fun getUserByPhone(phoneNumber: String): Flowable<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: UserModel): Completable

    @Update
    fun update(model: UserModel): Completable

    @Delete
    fun delete(model: UserModel): Completable
}