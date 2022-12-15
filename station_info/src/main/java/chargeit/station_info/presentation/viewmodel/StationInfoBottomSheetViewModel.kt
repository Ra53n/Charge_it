package chargeit.station_info.presentation.viewmodel

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.interactor.ElectricStationInteractor
import chargeit.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class StationInfoBottomSheetViewModel(private val interactor: ElectricStationInteractor,
    private val navigator: Navigator) : CoreViewModel() {

    private val _electricStationLiveData = MutableLiveData<List<ElectricStationEntity>>()
    val electricStationLiveData: LiveData<List<ElectricStationEntity>> by this::_electricStationLiveData

    fun navigateToFullStationInfo(bundle: Bundle) {
        navigator.navigateToFullStationInfo(bundle)
    }

    fun getAddressFromCoordinate(lat: Double, lon: Double, context: Context): String {
        val fullAddress = StringBuilder()
        val geocoder = Geocoder(context, Locale("RU"))
        val addresses = geocoder.getFromLocation(lat, lon, 1)

        if (addresses?.get(0)?.thoroughfare != null) fullAddress.append(addresses[0].thoroughfare)
        fullAddress.append(", ")
        if (addresses?.get(0)?.subThoroughfare != null) fullAddress.append(addresses[0].subThoroughfare)
        fullAddress.append(", ")
        if (addresses?.get(0)?.locality != null) fullAddress.append(addresses[0].locality)
        fullAddress.append("\n")
        if (addresses?.get(0)?.countryName != null) fullAddress.append(addresses[0].countryName)
        fullAddress.append(", ")
        if (addresses?.get(0)?.postalCode != null) fullAddress.append(addresses[0].postalCode)
        fullAddress.append(", ")

        return fullAddress.toString()
    }

    fun getElectricStationInfo(id: Int) {
        interactor.getElectricStationById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{_electricStationLiveData.value = it}
    }
}