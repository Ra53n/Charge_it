package chargeit.main_screen.domain

sealed class LocationState {
    data class Success(val deviceLocation: DeviceLocation) : LocationState()
    data class Error(val locationError: LocationError) : LocationState()
    object Loading : LocationState()
}