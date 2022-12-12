package chargeit.main_screen.domain

import chargeit.main_screen.domain.messages.FiltersMessage
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class MapState(
    var lastLocation: LatLng,
    var zoomLevel: Float,
    var addressMarker: MarkerOptions? = null,
    var locationMarker: MarkerOptions? = null,
    var chargeFilters: FiltersMessage.ChargeFilters? = null
)