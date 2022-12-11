package chargeit.main_screen.utils

import android.view.View
import android.view.WindowManager

class ViewHelper() {

    fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}