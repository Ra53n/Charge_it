package chargeit.core.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import chargeit.core.viewmodel.CoreViewModel

open class CoreFragment(layoutRes: Int) : Fragment(layoutRes) {
    protected open val viewModel: CoreViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.let { viewLifecycleOwner.lifecycle.addObserver(it) }
    }
}