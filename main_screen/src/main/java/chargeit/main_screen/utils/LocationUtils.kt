package chargeit.main_screen.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

private const val RESULT_SIZE = 3

fun getDistanceBetween(firstLocation: LatLng, secondLocation: LatLng): FloatArray {
    val distanceArray = FloatArray(RESULT_SIZE)
    Location.distanceBetween(
        firstLocation.latitude, firstLocation.longitude,
        secondLocation.latitude, secondLocation.longitude, distanceArray
    )
    return distanceArray
}