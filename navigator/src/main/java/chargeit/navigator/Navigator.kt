package chargeit.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment

interface Navigator {
    fun navigateToStationInfoBottomSheet(fragment: Fragment, bundle: Bundle)
}