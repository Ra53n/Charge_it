package chargeit.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController

interface Navigator {
    fun navigateToStationInfoBottomSheet(bundle: Bundle)
}