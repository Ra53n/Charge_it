package chargeit.main_screen.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(
    text: String = "",
    actionText: String = "",
    action: (View) -> Unit = {},
    length: Int = Snackbar.LENGTH_LONG,
    anchor: View? = null
) {
    Snackbar
        .make(this, text, length)
        .setAction(actionText, action)
        .setAnchorView(anchor)
        .show()
}

fun View.hideKeyboard() =
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())