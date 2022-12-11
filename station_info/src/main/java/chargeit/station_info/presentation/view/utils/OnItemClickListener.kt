package chargeit.station_info.presentation.view.utils

import chargeit.data.domain.model.Socket

interface OnItemClickListener {
    fun onItemClick(socket: Socket)
}
