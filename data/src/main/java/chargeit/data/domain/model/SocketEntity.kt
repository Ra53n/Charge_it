package chargeit.data.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SocketEntity(
    val id: Int,
    val socket: Socket,
    val status: Boolean,
) : Parcelable