package chargeit.app.room

import androidx.room.*

@Dao
interface ElectricStationDao {
    @Query("SELECT * FROM ElectricStationEntity")
    fun all(): List<CarEntity>

    @Query("SELECT * FROM ElectricStationEntity WHERE coordinates LIKE :coordinates")
    fun getDataByWord(coordinates: String): List<ElectricStationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: ElectricStationEntity)

    @Update
    fun update(entity: ElectricStationEntity)

    @Delete
    fun delete(entity: ElectricStationEntity)
}