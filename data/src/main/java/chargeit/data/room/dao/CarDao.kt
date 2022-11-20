package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.CarModel

@Dao
interface CarDao {

    @Query("SELECT * FROM CarModel")
    fun all(): List<CarModel>

    @Query("SELECT * FROM CarModel WHERE brand LIKE :brand")
    fun getCarByBrand(brand: String): List<CarModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: CarModel)

    @Update
    fun update(model: CarModel)

    @Delete
    fun delete(model: CarModel)
}