package chargeit.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import chargeit.data.room.Converter
import chargeit.data.room.baseElectricStationsList
import chargeit.data.room.carsList
import chargeit.data.room.dao.CarDao
import chargeit.data.room.dao.ElectricStationDao
import chargeit.data.room.dao.UserDao
import chargeit.data.room.model.CarModel
import chargeit.data.room.model.ElectricStationModel
import chargeit.data.room.model.UserModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@Database(
    entities = [CarModel::class, ElectricStationModel::class, UserModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val carDao: CarDao
    abstract val electricStationDao: ElectricStationDao
    abstract val userDao: UserDao

    companion object {
        private const val DB_NAME = "database.db"
        private var instance: AppDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            instance?.let {
                                it.electricStationDao.saveList(baseElectricStationsList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                            }
                            instance?.let {
                                it.carDao.saveList(carsList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                            }
                        }
                    })
                    .build()
            }
        }
    }
}