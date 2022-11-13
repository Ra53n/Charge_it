package chargeit.app.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import chargeit.app.room.dao.CarDao
import chargeit.app.room.entities.CarEntity


@Database(entities = [CarEntity::class], version = 1, exportSchema = false)

abstract class CarDatabase : RoomDatabase() {

    abstract val carDao: CarDao

    companion object {
        private const val DB_NAME = "car.db"
        private var instance: CarDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, CarDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}