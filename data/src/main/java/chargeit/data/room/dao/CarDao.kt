package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.CarModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CarDao {

    @Query("SELECT * FROM CarModel")
    fun all(): Flowable<List<CarModel>>

    @Query("SELECT * FROM CarModel WHERE brand LIKE :brand")
    fun getCarByBrand(brand: String): Flowable<List<CarModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: CarModel): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveList(modelList: List<CarModel>): Completable

    @Update
    fun update(model: CarModel): Completable

    @Delete
    fun delete(model: CarModel): Completable
}