package chargeit.profilescreen.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.State
import chargeit.data.interactor.UserInteractor
import chargeit.profilescreen.data.domain.SAVED_USER_DATA
import chargeit.profilescreen.data.mapper.UserMapper
import chargeit.profilescreen.data.model.LoginUiModel
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(
    private val userInteractor: UserInteractor,
    private val sharedPreferences: SharedPreferences,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val _loginLiveData = MutableLiveData<State>()
    val loginLiveData: LiveData<State> by this::_loginLiveData

    fun login(model: LoginUiModel) {
        userInteractor.loginUser(model.name, model.phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    sharedPreferences
                        .edit()
                        .putString(SAVED_USER_DATA, Gson().toJson(userMapper.map(it)))
                        .apply()
                    _loginLiveData.value = State.Success

                },
                {
                    _loginLiveData.value = State.Error
                }
            )
    }
}