package chargeit.station_info.ui.brief

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import chargeit.station_info.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class StationInfoBottomSheetDialog {

    fun showDialog(context: Context, distance: Double) {
        val bottomSheetDialog = BottomSheetDialog(context)

        bottomSheetDialog.setContentView(R.layout.fragment_station_info_bottom_sheet)

        val buttonDistance = bottomSheetDialog.findViewById<MaterialButton>(R.id.distance_button)
        val moreInfoButton = bottomSheetDialog.findViewById<MaterialButton>(R.id.more_info_button)
        val closeDialogImage = bottomSheetDialog.findViewById<ImageView>(R.id.close_sign_image_view)
        val addressText = bottomSheetDialog.findViewById<TextView>(R.id.station_address_text_view)
        buttonDistance?.text = context.getString(chargeit.core.R.string.length_unit_km_text, distance.toString())

        bottomSheetDialog.show()
    }


}