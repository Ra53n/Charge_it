package chargeit.data.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class SocketEntity(
    val id: UUID,
    val socket: Socket,
    var status: Boolean,
) : Parcelable