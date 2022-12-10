package chargeit.station_info.presentation.view.utils

import android.view.View
import chargeit.data.domain.model.Socket

interface OnItemClickListener {
    fun onItemClick(view: View, socket: Socket)
}
