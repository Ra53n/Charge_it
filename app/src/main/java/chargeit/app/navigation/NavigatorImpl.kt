package chargeit.app.navigation

import android.os.Bundle
import androidx.navigation.NavController
import chargeit.app.R
import chargeit.navigator.Navigator

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateToStationInfoBottomSheet(bundle: Bundle) {
        navController.navigate(R.id.station_info_bottom_sheet, bundle)
    }

    override fun navigateToFullStationInfo(bundle: Bundle) {
        navController.navigate(R.id.full_station_info, bundle)
    }

    override fun navigateToSocketInfo(bundle: Bundle) {
        navController.navigate(R.id.socket_info, bundle)
    }

    override fun navigateToLoginScreen() {
        navController.navigate(R.id.login_fragment)
    }

    override fun navigateToRegistrationScreen() {
        navController.navigate(R.id.profile_registration_fragment)
    }

    override fun navigateToSocketSelectionScreen() {
        navController.navigate(R.id.socket_selection_fragment)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}