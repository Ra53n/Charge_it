package chargeit.main_screen.ui.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.Socket
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.utils.convertSocketListToChargeFilterList

class FiltersFragmentViewModel : CoreViewModel() {

    private val _filtersLiveData = MutableLiveData<FiltersMessage>()
    val filtersLiveData: LiveData<FiltersMessage> by this::_filtersLiveData

    private var chargeFilters = FiltersMessage.ChargeFilters(
        filters = convertSocketListToChargeFilterList(Socket.getAllSockets())
    )

    fun requestFilters() {
        _filtersLiveData.value = chargeFilters
    }

    fun onMenuClick() {
        _filtersLiveData.value = FiltersMessage.SwitchAllOff
    }
}
