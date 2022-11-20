package chargeit.main_screen.domain.charge_stations

sealed class ChargeStationsState {
    data class Success(val chargeStations: List<ChargeStation>) : ChargeStationsState()
    data class Error(val chargeStationError: Throwable) : ChargeStationsState()
    object Loading : ChargeStationsState()
}