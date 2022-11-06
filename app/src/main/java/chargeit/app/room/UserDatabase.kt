package chargeit.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserEntity::class], version = 1, exportSchema = false)

abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        private const val DB_NAME = "user.db"
        private var instance: UserDatabase? = null
        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context?) {
            if (instance == null) {
                instance = Room.databaseBuilder(context!!, UserDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}