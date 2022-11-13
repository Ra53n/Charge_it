package chargeit.profilescreen.view

import chargeit.core.view.CoreFragment
import chargeit.profilescreen.R
import chargeit.profilescreen.viewmodel.ProfileViewModel

class ProfileFragment : CoreFragment(R.layout.fragment_profile) {

    override val viewModel = ProfileViewModel()

    companion object {
        fun newInstance() = ProfileFragment()
    }
}