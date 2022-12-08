package chargeit.station_info.presentation.view.utils

import chargeit.data.domain.model.ElectricStationEntity

interface OnButtonClickListener {

    fun onButtonClick(address: String)
}