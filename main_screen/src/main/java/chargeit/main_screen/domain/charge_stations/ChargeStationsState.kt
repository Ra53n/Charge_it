package chargeit.main_screen.domain.charge_stations

import chargeit.main_screen.domain.message.AppMessage

sealed class ChargeStationsState {
    data class Success(val chargeStations: List<ChargeStation>) : ChargeStationsState()
    data class Message(val message: AppMessage) : ChargeStationsState()
    object Loading : ChargeStationsState()
}