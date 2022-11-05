package chargeit.main_screen.domain

import chargeit.main_screen.settings.*
import com.google.android.gms.maps.model.LatLng

data class Place(
    val name: String = DEFAULT_PLACE_NAME,
    val tag: String = DEFAULT_PLACE_TAG,
    val coordinates: LatLng = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE),
    val zoomLevel: Float = DEFAULT_PLACE_INIT_ZOOM_LEVEL,
)
