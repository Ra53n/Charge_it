package chargeit.app.room.dao

import androidx.room.*
import chargeit.app.room.entities.SocketEntity

@Dao
interface SocketDao {
    @Query("SELECT * FROM SocketEntity")
    fun all(): List<SocketEntity>

    @Query("SELECT * FROM SocketEntity WHERE title LIKE :title")
    fun getSocketByTitle(title: String): List<SocketEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: SocketEntity)

    @Update
    fun update(entity: SocketEntity)

    @Delete
    fun delete(entity: SocketEntity)
}