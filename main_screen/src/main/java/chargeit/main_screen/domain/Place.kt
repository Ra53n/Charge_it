package chargeit.main_screen.domain

import com.google.android.gms.maps.model.LatLng

data class Place(
    val name: String = "",
    val tag: String = "",
    val coordinates: LatLng = LatLng(0.0, 0.0),
    val zoomLevel: Float = 9.0f,
)
