package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.UserModel

@Dao
interface UserDao {

    @Query("SELECT * FROM UserModel")
    fun all(): List<UserModel>

    @Query("SELECT * FROM UserModel WHERE phoneNumber LIKE :phoneNumber")
    fun getUserByPhone(phoneNumber: String): List<UserModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: UserModel)

    @Update
    fun update(model: UserModel)

    @Delete
    fun delete(model: UserModel)
}