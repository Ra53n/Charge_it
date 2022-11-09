package chargeit.main_screen.domain

import chargeit.main_screen.settings.DEFAULT_PLACE_LATITUDE
import chargeit.main_screen.settings.DEFAULT_PLACE_LONGITUDE
import chargeit.main_screen.settings.DEFAULT_PLACE_ZOOM_LEVEL
import com.google.android.gms.maps.model.LatLng

data class DefaultPlace(
    val coordinates: LatLng = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE),
    val zoomLevel: Float = DEFAULT_PLACE_ZOOM_LEVEL
)