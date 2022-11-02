package chargeit.app.room

import androidx.room.*

@Dao
interface SocketDao {
    @Query("SELECT * FROM SocketEntity")
    fun all(): List<CarEntity>

    @Query("SELECT * FROM SocketEntity WHERE title LIKE :title")
    fun getDataByWord(title: String): List<SocketEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: SocketEntity)

    @Update
    fun update(entity: SocketEntity)

    @Delete
    fun delete(entity: SocketEntity)
}