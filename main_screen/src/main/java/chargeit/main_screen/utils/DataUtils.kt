package chargeit.main_screen.utils

import android.content.Context
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.mappers.SocketModelToEntityMapper
import chargeit.data.room.model.ElectricStationModel
import chargeit.main_screen.R
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.domain.ChargeFilter
import chargeit.main_screen.domain.messages.FiltersMessage
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataUtils : KoinComponent {

    private val context: Context by inject()
    private val bitmapDescriptorUtils: BitmapDescriptorUtils by inject()
    private val socketMapper: SocketModelToEntityMapper by inject()

    fun getBitmapFromAvailableSource(
        vectorResID: Int,
        rasterResID: Int
    ): BitmapDescriptor =
        bitmapDescriptorUtils.getBitmapDescriptorFromVector(vectorResID)
            ?: BitmapDescriptorFactory.fromResource(rasterResID)

    fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun getChargeStationClusterItem(
        title: String,
        location: LatLng,
        snippet: String,
        entity: ElectricStationEntity
    ): MarkerClusterItem {
        val bitmapDescriptor = getBitmapFromAvailableSource(
            R.drawable.ic_station_marker,
            R.drawable.ic_station_marker_backup
        )
        return MarkerClusterItem(location, title, snippet, bitmapDescriptor, entity)
    }

    private fun convertElectricStationModelToClusterItem(
        model: ElectricStationModel
    ): MarkerClusterItem {
        val mapper = ElectricStationModelToEntityMapper(socketMapper)
        val entity = mapper.map(model)
        return getChargeStationClusterItem(
            title = entity.titleStation,
            location = LatLng(entity.lat, entity.lon),
            snippet = String.EMPTY,
            entity = entity
        )
    }

    fun convertModelsToCLusterItems(models: List<ElectricStationModel>) =
        models.map { model ->
            convertElectricStationModelToClusterItem(model)
        }

    companion object {

        fun getMatchSocketsCount(
            model: ElectricStationModel,
            chargeFilters: FiltersMessage.ChargeFilters
        ) = model.listOfSockets.count { socketModel ->
            chargeFilters.filters.count { filter -> filter.id == socketModel.socket.id && filter.isChecked } > Int.ZERO
        }

        fun convertSocketListToChargeFilterList(sockets: List<Socket>) =
            sockets.map { socket ->
                ChargeFilter(socket.id, socket.icon, socket.title)
            }
    }
}