package chargeit.station_info.presentation.viewmodel

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import chargeit.core.viewmodel.CoreViewModel
import chargeit.navigator.Navigator
import java.util.*

class StationInfoBottomSheetViewModel (private val navigator: Navigator) : CoreViewModel() {

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
}