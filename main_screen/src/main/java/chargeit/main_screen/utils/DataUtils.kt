package chargeit.main_screen.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.model.ElectricStationModel
import chargeit.main_screen.R
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.domain.ChargeFilter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

fun getBitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(Int.ZERO, Int.ZERO, intrinsicWidth, intrinsicHeight)
        val bitmap =
            Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

fun getBitmapFromAvailableSource(
    context: Context,
    vectorResID: Int,
    rasterResID: Int
): BitmapDescriptor =
    getBitmapDescriptorFromVector(context, vectorResID)
        ?: BitmapDescriptorFactory.fromResource(rasterResID)

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}

fun convertSocketListToChargeFilterList(sockets: List<Socket>) =
    sockets.map { socket ->
        ChargeFilter(socket.id, socket.icon, socket.title)
    }

fun getChargeStationClusterItem(
    context: Context,
    title: String,
    location: LatLng,
    snippet: String,
    entity: ElectricStationEntity
): MarkerClusterItem {
    val bitmapDescriptor = getBitmapFromAvailableSource(
        context, R.drawable.ic_station_marker,
        R.drawable.ic_station_marker_backup
    )
    return MarkerClusterItem(location, title, snippet, bitmapDescriptor, entity)
}

fun convertElectricStationModelToClusterItem(
    context: Context,
    model: ElectricStationModel
): MarkerClusterItem {
    val mapper = ElectricStationModelToEntityMapper()
    val entity = mapper.map(model)
    return getChargeStationClusterItem(
        context = context,
        title = entity.titleStation,
        location = LatLng(entity.lat, entity.lon),
        snippet = String.EMPTY,
        entity = entity
    )
}

fun convertModelsToCLusterItems(context: Context, models: List<ElectricStationModel>) =
    models.map { model ->
        convertElectricStationModelToClusterItem(context, model)
    }

fun getMatchSocketsCount(model: ElectricStationModel, chargeFilters: FiltersMessage.ChargeFilters) =
    model.listOfSockets.count { socket ->
        chargeFilters.filters.count { filter -> filter.id == socket.id && filter.isChecked } > Int.ZERO
    }
