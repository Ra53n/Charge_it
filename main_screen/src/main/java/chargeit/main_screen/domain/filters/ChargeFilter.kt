package chargeit.main_screen.domain.filters

import android.graphics.drawable.Drawable

data class ChargeFilter(
    val id: Int,
    val icon: Drawable,
    val title: String,
    var isChecked: Boolean = true
)
