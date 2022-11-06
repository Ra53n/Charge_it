package chargeit.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ElectricStationEntity::class], version = 1, exportSchema = false)

abstract class ElectricStationDatabase : RoomDatabase() {

    abstract val electricStationDao: ElectricStationDao

    companion object {
        private const val DB_NAME = "electricStation.db"
        private var instance: ElectricStationDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context?) {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context!!, ElectricStationDatabase::class.java, DB_NAME)
                        .build()
            }
        }
    }
}