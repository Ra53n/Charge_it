package chargeit.data.room.dao

import androidx.room.*
import chargeit.data.room.model.ElectricStationModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ElectricStationDao {

    @Query("SELECT * FROM ElectricStationModel")
    fun all(): Flowable<List<ElectricStationModel>>

    @Query("SELECT * FROM ElectricStationModel WHERE id LIKE :id")
    fun getElectricStationById(id: Int): Flowable<List<ElectricStationModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: ElectricStationModel): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveList(modelList: List<ElectricStationModel>): Completable

    @Update
    fun update(model: ElectricStationModel): Completable

    @Delete
    fun delete(model: ElectricStationModel): Completable
}