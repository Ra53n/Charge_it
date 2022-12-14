package chargeit.app.presentation.view

import android.os.Bundle
import android.view.View
import chargeit.app.R
import chargeit.app.presentation.viewmodel.MainViewModel
import chargeit.core.utils.EMPTY
import chargeit.core.utils.PROFILE_REGISTRATION
import chargeit.core.view.CoreFragment
import chargeit.profilescreen.view.fragent.ProfileFragment
import chargeit.profilescreen.view.fragent.ProfileRegistrationFragment

class MainFragment : CoreFragment(R.layout.main_fragment) {

    override val viewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            PROFILE_REGISTRATION,
            viewLifecycleOwner
        ) { _, _ ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileRegistrationFragment.newInstance())
                .addToBackStack(String.EMPTY)
                .commit()
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ProfileFragment.newInstance())
            .commit()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}