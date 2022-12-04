package chargeit.main_screen.data

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ChargeStationIconRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MarkerClusterItem>
) : DefaultClusterRenderer<MarkerClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: MarkerClusterItem,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(item.getIcon())
        markerOptions.title(item.title)
        markerOptions.snippet(item.snippet)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}