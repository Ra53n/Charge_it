package chargeit.main_screen.data

import chargeit.data.domain.model.ElectricStationEntity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerClusterItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String,
    private val icon: BitmapDescriptor,
    private val entity: ElectricStationEntity
) : ClusterItem {

    override fun getPosition() = position

    override fun getTitle() = title

    override fun getSnippet() = snippet

    fun getIcon() = icon

    fun getEntity() = entity
}