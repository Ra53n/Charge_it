package chargeit.main_screen.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeFilter(
    val id: Int,
    val icon: Int,
    val title: String,
    var isChecked: Boolean = true
) : Parcelable


