package chargeit.station_info.presentation.view.utils

import chargeit.data.domain.model.SocketEntity

interface OnItemClickListener {
    fun onItemClick(socket: SocketEntity)
}
