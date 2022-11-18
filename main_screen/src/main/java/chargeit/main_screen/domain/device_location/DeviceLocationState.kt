package chargeit.main_screen.domain.device_location

sealed class DeviceLocationState {
    data class Success(val deviceLocation: DeviceLocation) : DeviceLocationState()
    data class Error(val deviceLocationError: DeviceLocationError) : DeviceLocationState()
    data class Event(val deviceLocationEvent: DeviceLocationEvent) : DeviceLocationState()
    object Loading : DeviceLocationState()
}