package chargeit.main_screen.domain.charge_stations

import chargeit.data.domain.model.ElectricStationEntity
import com.google.android.gms.maps.model.MarkerOptions

data class ChargeStation(
    val info: ElectricStationEntity,
    val markerOptions: MarkerOptions = MarkerOptions()
)
