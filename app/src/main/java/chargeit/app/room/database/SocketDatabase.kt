package chargeit.app.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import chargeit.app.room.dao.SocketDao
import chargeit.app.room.entities.SocketEntity


@Database(entities = [SocketEntity::class], version = 1, exportSchema = false)

abstract class SocketDatabase : RoomDatabase() {

    abstract val socketDao: SocketDao

    companion object {
        private const val DB_NAME = "socket.db"
        private var instance: SocketDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, SocketDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}