package chargeit.main_screen.domain.messages

import chargeit.data.domain.model.ElectricStationEntity
import chargeit.main_screen.data.MarkerClusterItem
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

sealed class AppMessage {
    data class StationInfo(val entity: ElectricStationEntity, val distance: Double) : AppMessage()
    object Filters : AppMessage()
    data class ChargeStationMarkers(val clusterItems: List<MarkerClusterItem>) : AppMessage()
    data class AddressMarker(val markerOptions: MarkerOptions) : AppMessage()
    data class MoveCamera(val animated: Boolean, val location: LatLng, val zoomLevel: Float) :
        AppMessage()

    data class InfoDialog(val title: String, val message: String) : AppMessage()
    data class InfoSnackBar(val text: String) : AppMessage()
    data class InfoToast(val text: String, val length: Int) : AppMessage()
}
