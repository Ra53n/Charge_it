package chargeit.navigator

import android.os.Bundle

interface Navigator {

    fun navigateToStationInfoBottomSheet(bundle: Bundle)

    fun navigateToFullStationInfo(bundle: Bundle)

    fun navigateToSocketInfo(bundle: Bundle)

    fun navigateToLoginScreen()

    fun navigateToRegistrationScreen()

    fun navigateToSocketSelectionScreen()

    fun navigateUp()
}