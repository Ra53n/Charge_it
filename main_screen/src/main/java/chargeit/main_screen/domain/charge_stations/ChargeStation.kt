package chargeit.main_screen.domain.charge_stations

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.main_screen.data.MarkerClusterItem

data class ChargeStation(
    val info: ElectricStationEntity,
    val clusterItem: MarkerClusterItem
)
