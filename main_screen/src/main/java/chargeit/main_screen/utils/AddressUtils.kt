package chargeit.main_screen.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun getAddressByLocation(
    context: Context,
    location: LatLng,
    maxResults: Int,
    message: String
): Address = (Geocoder(context).getFromLocation(location.latitude, location.longitude, maxResults)
    ?: listOf(Address(Locale(message)))).first()

fun getAddressesByQuery(context: Context, query: String, maxResults: Int) =
    Geocoder(context).getFromLocationName(query, maxResults) ?: listOf()