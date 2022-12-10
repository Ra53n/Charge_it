package chargeit.app.navigation

import android.os.Bundle
import androidx.navigation.NavController
import chargeit.app.R
import chargeit.navigator.Navigator

class NavigatorImpl(private val navController: NavController): Navigator {
    override fun navigateToStationInfoBottomSheet(bundle: Bundle) {
        navController.navigate(R.id.station_info_bottom_sheet, bundle)
    }
}