package chargeit.main_screen.domain.device_location

import android.location.Address
import chargeit.core.utils.EMPTY
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

data class DeviceLocation(
    val address: Address = Address(Locale(String.EMPTY)),
    val markerOptions: MarkerOptions = MarkerOptions()
)
