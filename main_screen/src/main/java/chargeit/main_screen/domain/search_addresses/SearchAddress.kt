package chargeit.main_screen.domain.search_addresses

import android.location.Address
import chargeit.core.utils.EMPTY
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

data class SearchAddress(
    val address: Address = Address(Locale(String.EMPTY)),
    val markerOptions: MarkerOptions = MarkerOptions()
)