package chargeit.main_screen.settings

import android.Manifest
import android.location.LocationManager

// Address search
const val ADDRESS_SEARCH_ZOOM_LEVEL = 10.0f
const val MAX_ADDRESS_SEARCH_RESULTS = 5

// Device location
const val DEVICE_LOCATION_REFRESH_PERIOD = 6000L
const val DEVICE_LOCATION_MINIMAL_DISTANCE = 100f
const val DEVICE_LOCATION_ZOOM_LEVEL = 15f
const val DEVICE_LOCATION_SEARCH_RESULTS = 1

// Default place
const val DEFAULT_PLACE_LATITUDE = 55.751513
const val DEFAULT_PLACE_LONGITUDE = 37.616655
const val DEFAULT_PLACE_ZOOM_LEVEL = 10.0f

// Location errors
const val PERMISSION_ERROR_ID = 1
const val NO_PROVIDER_LOCATION_PRESENT_ERROR_ID = 2
const val NO_PROVIDER_LOCATION_NOT_PRESENT_ERROR_ID = 3

// Other
const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val PROVIDER = LocationManager.NETWORK_PROVIDER
