package chargeit.main_screen.ui.filters

import androidx.lifecycle.LiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.Socket
import chargeit.main_screen.data.contracts.FiltersFragmentViewModelContract
import chargeit.main_screen.data.SingleLiveEvent
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.utils.DataUtils

class FiltersFragmentViewModel : CoreViewModel(), FiltersFragmentViewModelContract {

    private val _filtersLiveData = SingleLiveEvent<FiltersMessage>()
    val filtersLiveData: LiveData<FiltersMessage> by this::_filtersLiveData

    private var chargeFilters = FiltersMessage.ChargeFilters(
        filters = DataUtils.convertSocketListToChargeFilterList(Socket.getAllSockets())
    )

    override fun requestFilters() {
        _filtersLiveData.value = chargeFilters
    }

    override fun onMenuClick() {
        _filtersLiveData.value = FiltersMessage.SwitchAllOff
    }
}
