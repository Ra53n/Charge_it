package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.ElectricStationModel

@Dao
interface ElectricStationDao {

    @Query("SELECT * FROM ElectricStationModel")
    fun all(): List<ElectricStationModel>

    @Query("SELECT * FROM ElectricStationModel WHERE id LIKE :id")
<<<<<<< HEAD
    fun getElectricStationById(id: String): List<ElectricStationModel>
=======
    fun getElectricStationById(id: Int): List<ElectricStationModel>
>>>>>>> origin/feature_data

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: ElectricStationModel)

    @Update
    fun update(model: ElectricStationModel)

    @Delete
    fun delete(model: ElectricStationModel)
}