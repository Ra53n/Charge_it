package chargeit.data.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SocketEntity(
    val id: Int,
    val socket: Socket,
    var status: Boolean,
) : Parcelable