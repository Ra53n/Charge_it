package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.SocketModel

@Dao
interface SocketDao {
    @Query("SELECT * FROM SocketModel")
    fun all(): List<SocketModel>

    @Query("SELECT * FROM SocketModel WHERE title LIKE :title")
    fun getSocketByTitle(title: String): List<SocketModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: SocketModel)

    @Update
    fun update(model: SocketModel)

    @Delete
    fun delete(model: SocketModel)
}