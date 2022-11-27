package chargeit.profilescreen.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.profilescreen.data.domain.SAVED_USER_DATA
import chargeit.profilescreen.data.model.UserUiModel
import com.google.gson.Gson

class ProfileViewModel(
    private val sharedPreferences: SharedPreferences
) : CoreViewModel() {

    private val _profileLiveData = MutableLiveData<UserUiModel?>()
    val profileLiveData: LiveData<UserUiModel?> by this::_profileLiveData

    fun checkUserData() {
        val userUiModel = Gson().fromJson(
            sharedPreferences.getString(SAVED_USER_DATA, ""),
            UserUiModel::class.java
        )
        _profileLiveData.value = userUiModel
    }

    fun logout() {
        sharedPreferences.edit()
            .clear()
            .apply()
        _profileLiveData.value = null
    }
}