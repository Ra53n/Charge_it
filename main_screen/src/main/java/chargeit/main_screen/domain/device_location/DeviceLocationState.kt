package chargeit.main_screen.domain.device_location

import chargeit.main_screen.domain.message.AppMessage

sealed class DeviceLocationState {
    data class Success(val location: DeviceLocation) : DeviceLocationState()
    data class Message(val message: AppMessage) : DeviceLocationState()
    object Loading : DeviceLocationState()
}