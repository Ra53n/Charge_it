package chargeit.station_info.ui.brief

import chargeit.data.domain.model.ElectricStationEntity

interface OnButtonClickListener {

    fun onButtonClick(address: String)
}