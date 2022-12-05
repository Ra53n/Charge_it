package chargeit.station_info.ui.full

import android.view.View
import chargeit.data.domain.model.Socket

interface OnItemClickListener {
    fun onItemClick(view: View, socket: Socket)
}
