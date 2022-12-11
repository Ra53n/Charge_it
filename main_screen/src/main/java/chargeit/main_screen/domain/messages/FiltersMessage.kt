package chargeit.main_screen.domain.messages

import android.os.Parcelable
import chargeit.main_screen.domain.ChargeFilter
import kotlinx.android.parcel.Parcelize

sealed class FiltersMessage {
    @Parcelize
    data class ChargeFilters(val filters: List<ChargeFilter>) : FiltersMessage(), Parcelable
    object SwitchAllOff : FiltersMessage()
}
