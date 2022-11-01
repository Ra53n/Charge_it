package chargeit.main_screen.domain

import com.google.android.gms.maps.model.LatLng

data class Place(
    val name: String,
    val tag: String,
    val coordinates: LatLng,
    val zoomLevel: Float,
)
