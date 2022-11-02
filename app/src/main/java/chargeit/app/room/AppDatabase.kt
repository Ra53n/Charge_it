package chargeit.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CarEntity::class, ElectricStationEntity::class, SocketEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract val carDao: CarDao
    abstract val electricStationDao: ElectricStationDao
    abstract val socketDao: SocketDao
    abstract val userDao: UserDao

    companion object {
        private const val DB_NAME = "database.db"
        private var instance: AppDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context?) {
            if (instance == null) {
                instance = Room.databaseBuilder(context!!, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}