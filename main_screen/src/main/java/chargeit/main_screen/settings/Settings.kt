package chargeit.main_screen.settings

import android.Manifest

// Location messages
const val PERMISSION_ERROR_ID = 1
const val GOOGLE_PLAY_SERVICES_NOT_PRESENT_ID = 2
const val LOCATION_IS_NOT_AVAILABLE_ID = 3
const val LOCATION_IS_AVAILABLE_ID = 4
const val LOCATION_ERROR_ID = 5

// Address messages
const val EMPTY_QUERY_ID = 1
const val ADDRESS_NOT_FOUND_ID = 2
const val ADDRESS_SEARCH_ERROR_ID = 3

// Station messages
const val STATIONS_REQUEST_ERROR_ID = 1

// Other
const val COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
const val FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
