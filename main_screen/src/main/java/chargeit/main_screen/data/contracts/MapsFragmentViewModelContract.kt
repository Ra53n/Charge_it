package chargeit.main_screen.data.contracts

import android.os.Bundle
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.domain.messages.FiltersMessage
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster

interface MapsFragmentViewModelContract {
    fun requestAddressSearch(query: String?)
    fun startLocationUpdates()
    fun stopLocationUpdates()
    fun requestChargeStations(chargeFilters: FiltersMessage.ChargeFilters?)
    fun onFilterScreenButtonClick()
    fun onZoomInButtonClick(location: LatLng, oldZoom: Float)
    fun onZoomOutButtonClick(location: LatLng, oldZoom: Float)
    fun onDeviceLocationButtonClick()
    fun onClusterItemClick(clusterItem: MarkerClusterItem): Boolean
    fun onClusterClick(cluster: Cluster<MarkerClusterItem>?, currentZoom: Float): Boolean
    fun onLocationMarkerClick(location: LatLng): Boolean
    fun onAddressMarkerClick(marker: Marker): Boolean
    fun requestNotGrantedNoAskDialog()
    fun navigateToStationInfoBottomSheet(bundle: Bundle)
    fun requestRationaleDialog()
    fun restoreMapState()
    fun saveCameraState(location: LatLng, zoomLevel: Float)
}