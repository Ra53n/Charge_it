package chargeit.station_info.presentation.view.utils

import android.view.View
import chargeit.data.domain.model.Socket

interface OnItemClickListener {
    fun onItemClick(socket: Socket)
}
