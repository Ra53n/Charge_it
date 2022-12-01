package chargeit.main_screen.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import chargeit.core.utils.ZERO
import chargeit.data.domain.model.Socket
import chargeit.main_screen.domain.filters.ChargeFilter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

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

fun convertSocketListToChargeFilterList(context: Context, sockets: List<Socket>) =
    sockets.map { socket ->
        ChargeFilter(
            id = socket.id,
            icon = context.resources.getDrawable(socket.icon, null),
            title = socket.title
        )
    }