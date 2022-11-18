package chargeit.main_screen.settings

import android.Manifest

// Location errors
const val PERMISSION_ERROR_ID = 1
const val GOOGLE_PLAY_SERVICES_NOT_PRESENT_ERROR_ID = 2
const val LOCATION_IS_NOT_AVAILABLE_ERROR_ID = 3

// Location events
const val LOCATION_IS_AVAILABLE_EVENT_ID = 1

// Address errors
const val EMPTY_QUERY_ERROR_ID = 1
const val ADDRESS_NOT_FOUND_ERROR_ID = 2

// Other
const val COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
const val FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
