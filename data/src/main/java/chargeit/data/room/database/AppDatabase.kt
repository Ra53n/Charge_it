package chargeit.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
<<<<<<< HEAD
import chargeit.data.room.dao.CarDao
import chargeit.data.room.dao.ElectricStationDao
import chargeit.data.room.dao.SocketDao
import chargeit.data.room.dao.UserDao
import chargeit.data.room.model.CarModel
import chargeit.data.room.model.ElectricStationModel
import chargeit.data.room.model.SocketModel
import chargeit.data.room.model.UserModel

@Database(entities = [CarModel::class, SocketModel::class, ElectricStationModel::class, UserModel::class],
    version = 1,
    exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract val carDao: CarDao
    abstract val socketDao: SocketDao
=======
import androidx.room.TypeConverters
import chargeit.data.room.Converter
import chargeit.data.room.dao.CarDao
import chargeit.data.room.dao.ElectricStationDao
import chargeit.data.room.dao.UserDao
import chargeit.data.room.model.CarModel
import chargeit.data.room.model.ElectricStationModel
import chargeit.data.room.model.UserModel

@Database(
    entities = [CarModel::class, ElectricStationModel::class, UserModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val carDao: CarDao
>>>>>>> origin/feature_data
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
                    .build()
            }
        }
    }
}