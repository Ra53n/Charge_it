package chargeit.app.room

import androidx.room.*

@Dao
interface CarDao {

    @Query("SELECT * FROM CarEntity")
    fun all(): List<CarEntity>

    @Query("SELECT * FROM CarEntity WHERE brand LIKE :brand")
    fun getCarByBrand(brand: String): List<CarEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: CarEntity)

    @Update
    fun update(entity: CarEntity)

    @Delete
    fun delete(entity: CarEntity)
}