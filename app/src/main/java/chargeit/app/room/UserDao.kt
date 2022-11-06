package chargeit.app.room

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity")
    fun all(): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE phoneNumber LIKE :phoneNumber")
    fun getUserByPhone(phoneNumber: String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: UserEntity)

    @Update
    fun update(entity: UserEntity)

    @Delete
    fun delete(entity: UserEntity)
}