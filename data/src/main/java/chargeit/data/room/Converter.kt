package chargeit.data.room

import androidx.room.TypeConverter
import chargeit.data.domain.model.Socket
import chargeit.data.room.model.CarModel
import chargeit.data.room.model.SocketModel
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
    fun fromSocketModel(value: SocketModel?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromSocketModelString(value: String?): SocketModel? {
        val socketModel: Type = object : TypeToken<SocketModel?>() {}.type
        return Gson().fromJson(value, socketModel)
    }

    @TypeConverter
    fun fromSocketModelList(value: List<SocketModel>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromSocketModelListString(value: String?): List<SocketModel>? {
        val socketModelList: Type = object : TypeToken<List<SocketModel>?>() {}.type
        return Gson().fromJson(value, socketModelList)
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