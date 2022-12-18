package chargeit.main_screen.utils

import android.content.Context
import chargeit.main_screen.data.ChargeStationIconRenderer
import chargeit.main_screen.data.contracts.MapsFragmentViewModelContract
import chargeit.main_screen.data.MarkerClusterItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager

class MapHelper(
    private val context: Context,
) {

    private lateinit var clusterManager: ClusterManager<MarkerClusterItem>
    private lateinit var locationMarkerCollection: MarkerManager.Collection
    private lateinit var addressMarkerCollection: MarkerManager.Collection

    private fun initClustering(map: GoogleMap) {
        clusterManager = ClusterManager(context, map)
        locationMarkerCollection = clusterManager.markerManager.newCollection()
        addressMarkerCollection = clusterManager.markerManager.newCollection()
        clusterManager.renderer = ChargeStationIconRenderer(context, map, clusterManager)
    }

    private fun setMapUI(map: GoogleMap) {
        with(map.uiSettings) {
            isMyLocationButtonEnabled = false
            isRotateGesturesEnabled = false
            isMapToolbarEnabled = false
            isTiltGesturesEnabled = false
        }
    }

    private fun setMapListeners(map: GoogleMap, viewModel: MapsFragmentViewModelContract) {
        map.setOnCameraIdleListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { clusterItem ->
            viewModel.onClusterItemClick(clusterItem)
        }
        clusterManager.setOnClusterClickListener { cluster ->
            viewModel.onClusterClick(cluster, map.cameraPosition.zoom)
        }
        locationMarkerCollection.setOnMarkerClickListener { marker ->
            viewModel.onLocationMarkerClick(marker.position)
        }
        addressMarkerCollection.setOnMarkerClickListener { marker ->
            viewModel.onAddressMarkerClick(marker)
        }
    }

    fun initMap(map: GoogleMap, viewModel: MapsFragmentViewModelContract) {
        setMapUI(map)
        initClustering(map)
        setMapListeners(map, viewModel)
        viewModel.restoreMapState()
    }

    fun changeStationMarkers(items: List<MarkerClusterItem>) {
        with(clusterManager) {
            clearItems()
            addItems(items)
            cluster()
        }
    }

    fun changeLocationMarker(markerOptions: MarkerOptions) {
        locationMarkerCollection.clear()
        locationMarkerCollection.addMarker(markerOptions)
    }

    fun changeAddressMarker(markerOptions: MarkerOptions) {
        addressMarkerCollection.clear()
        addressMarkerCollection.addMarker(markerOptions)
    }

    fun moveCamera(map: GoogleMap, animated: Boolean, location: LatLng, zoomLevel: Float) {
        val cameraProperties = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
        if (animated) {
            map.animateCamera(cameraProperties)
        } else {
            map.moveCamera(cameraProperties)
        }
    }
}