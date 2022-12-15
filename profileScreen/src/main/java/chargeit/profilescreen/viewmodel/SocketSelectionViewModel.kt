package chargeit.profilescreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.Socket
import chargeit.profilescreen.view.adapter.SocketItem

class SocketSelectionViewModel : CoreViewModel() {
    private val _socketsLiveData = MutableLiveData<List<SocketItem>>()
    val socketsLiveData: LiveData<List<SocketItem>> by this::_socketsLiveData

    private val _filteredSocketsLiveData = MutableLiveData<List<SocketItem>>()
    val filteredSocketsLiveData: LiveData<List<SocketItem>> by this::_filteredSocketsLiveData

    private val socketList = Socket.getAllSockets().map { SocketItem(it, false) }


    fun requireSocketsList() {
        _socketsLiveData.value = Socket.getAllSockets().map { SocketItem(it, false) }
    }

    fun filterSockets(text: String) {
        _filteredSocketsLiveData.value =
            socketList.filter { it.socket.title.contains(text, ignoreCase = true) }
    }
}