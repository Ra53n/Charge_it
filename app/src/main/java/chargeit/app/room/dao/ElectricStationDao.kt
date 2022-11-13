package chargeit.app.room.dao

import androidx.room.*
import chargeit.app.room.entities.ElectricStationEntity

@Dao
interface ElectricStationDao {

    @Query("SELECT * FROM ElectricStationEntity")
    fun all(): List<ElectricStationEntity>

    @Query("SELECT * FROM ElectricStationEntity WHERE id LIKE :id")
    fun getElectricStationById(id: String): List<ElectricStationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: ElectricStationEntity)

    @Update
    fun update(entity: ElectricStationEntity)

    @Delete
    fun delete(entity: ElectricStationEntity)
}