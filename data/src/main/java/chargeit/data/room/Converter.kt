package chargeit.data.room

import androidx.room.TypeConverter
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.CarModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converter {
    @TypeConverter
    fun fromSocket(value: List<Socket>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromSocketString(value: String?): List<Socket>? {
        val socket: Type = object : TypeToken<List<Socket>?>() {}.type
        return Gson().fromJson(value, socket)
    }

    @TypeConverter
    fun fromCarModel(value: CarModel?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromCarModelString(value: String?): CarModel? {
        val car: Type = object : TypeToken<CarModel?>() {}.type
        return Gson().fromJson(value, car)
    }
}