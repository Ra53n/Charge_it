package chargeit.data.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import chargeit.core.R

@Parcelize
class Socket(
    val id: Int,
    val icon: Int,
    val title: String,
    val description: String,
) : Parcelable {
    companion object {
        // В это поле нужно вносить все виды разъемов
        private val socketList = listOf<Socket>(
            Socket(1, R.drawable.ccs_combo_1, "DC CCS - Type 1", ""),
            Socket(2, R.drawable.ccs_combo_2, "DC CCS - Type 2", ""),
            Socket(3, R.drawable.chademo, "DC CHAdeMO", ""),
            Socket(4, R.drawable.gbt_dc, "DC GB/T", ""),
            Socket(5, R.drawable.tesla_supercharger, "DC Tesla Supercharger", ""),
            Socket(6, R.drawable.gbt_ac, "AC GB/T", ""),
            Socket(7, R.drawable.type_1_j1772, "AC Type 1 - J1772", ""),
            Socket(8, R.drawable.type_2_mannekes, "AC Type 2", "")
        )

        val EMPTY_SOCKET = Socket(0, 0, "", "")

        fun getAllSockets() = socketList

        fun valueOf(id: Int) = socketList.find { it.id == id } ?: EMPTY_SOCKET
    }
}




