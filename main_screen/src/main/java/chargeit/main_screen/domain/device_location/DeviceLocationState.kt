package chargeit.main_screen.domain.device_location

sealed class DeviceLocationState {
    data class Success(val location: DeviceLocation) : DeviceLocationState()
    data class Error(val error: DeviceLocationError) : DeviceLocationState()
    data class Event(val event: DeviceLocationEvent) : DeviceLocationState()
    object Loading : DeviceLocationState()
}