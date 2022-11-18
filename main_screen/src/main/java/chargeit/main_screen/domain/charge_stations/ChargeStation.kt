package chargeit.main_screen.domain.charge_stations

import com.google.android.gms.maps.model.MarkerOptions

data class ChargeStation(
    val stationID: Int,
    val markerOptions: MarkerOptions = MarkerOptions()
)
