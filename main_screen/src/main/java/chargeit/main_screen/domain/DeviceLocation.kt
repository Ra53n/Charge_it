package chargeit.main_screen.domain

import android.location.Address
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import com.google.android.gms.maps.model.LatLng
import java.util.*

data class DeviceLocation(
    val location: LatLng = LatLng(Double.ZERO, Double.ZERO),
    val address: Address = Address(Locale(String.EMPTY))
)
