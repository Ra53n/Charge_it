package chargeit.core.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import chargeit.core.R
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.core.viewmodel.CoreViewModel
import com.google.android.material.snackbar.Snackbar

open class CoreFragment(layoutRes: Int) : Fragment(layoutRes) {
    protected open val viewModel: CoreViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.let { viewLifecycleOwner.lifecycle.addObserver(it) }
    }

    fun hideKeyboard(context: FragmentActivity) {
        val view = context.currentFocus
        view?.let {
            val manager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
            manager?.hideSoftInputFromWindow(view.windowToken, Int.ZERO)
        }
    }

    fun makeSnackbar(
        view: View,
        text: String = String.EMPTY,
        actionText: String = String.EMPTY,
        action: (View) -> Unit = {},
        length: Int = Snackbar.LENGTH_LONG,
        anchor: View? = null
    ) {
        Snackbar
            .make(view, text, length)
            .setAction(actionText, action)
            .setAnchorView(anchor)
            .setBackgroundTint(resources.getColor(R.color.grey_800))
            .setTextColor(resources.getColor(R.color.white))
            .show()
    }

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = context.getString(R.string.default_dialog_positive_button)
    ) {
        AlertDialog.Builder(context, R.style.AlertDialogStyle)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText)
            { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
