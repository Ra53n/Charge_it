package chargeit.main_screen.ui.filters

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.Socket
import chargeit.main_screen.domain.filters.ChargeFilter
import chargeit.main_screen.utils.convertSocketListToChargeFilterList

class FiltersFragmentViewModel(private val application: Application) : CoreViewModel() {

    private val _filtersLiveData = MutableLiveData<List<ChargeFilter>>()
    val filtersLiveData: LiveData<List<ChargeFilter>> by this::_filtersLiveData
    private val _mapFiltersLiveData = MutableLiveData<List<ChargeFilter>>()
    val mapFiltersLiveData: LiveData<List<ChargeFilter>> by this::_mapFiltersLiveData
    private var chargeFilters: List<ChargeFilter>

    init {
        chargeFilters = convertSocketListToChargeFilterList(application, Socket.getAllSockets())
    }

    fun requestFilters() {
        _filtersLiveData.postValue(chargeFilters)
    }

    fun saveFilters(filters: List<ChargeFilter>) {
        chargeFilters = filters
        _mapFiltersLiveData.postValue(chargeFilters)
    }
}
