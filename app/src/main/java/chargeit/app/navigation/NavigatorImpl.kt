package chargeit.app.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import chargeit.app.R
import chargeit.navigator.Navigator

class NavigatorImpl: Navigator {
    override fun navigateToStationInfoBottomSheet(fragment: Fragment, bundle: Bundle) {
        findNavController(fragment).navigate(R.id.station_info_bottom_sheet, bundle)
    }
}