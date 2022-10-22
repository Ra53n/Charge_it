package chargeit.app.presentation.view

import chargeit.app.R
import chargeit.app.presentation.viewmodel.MainViewModel
import chargeit.core.view.CoreFragment

class MainFragment : CoreFragment(R.layout.main_fragment) {

    override val viewModel = MainViewModel()

    companion object{
        fun newInstance() = MainFragment()
    }
}