package chargeit.profilescreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.Socket

class SocketSelectionViewModel : CoreViewModel() {
    private val _socketsLiveData = MutableLiveData<List<Socket>>()
    val socketsLiveData: LiveData<List<Socket>> by this::_socketsLiveData

    fun requireSocketsList() {
        _socketsLiveData.value = Socket.getAllSockets()
    }
}