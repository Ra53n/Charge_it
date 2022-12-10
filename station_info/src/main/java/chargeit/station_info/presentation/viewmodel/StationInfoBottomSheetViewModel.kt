package chargeit.station_info.presentation.viewmodel

import android.os.Bundle
import chargeit.core.viewmodel.CoreViewModel
import chargeit.navigator.Navigator

class StationInfoBottomSheetViewModel (private val navigator: Navigator) : CoreViewModel() {

    fun navigateToFullStationInfo(bundle: Bundle) {
        navigator.navigateToFullStationInfo(bundle)
    }
}