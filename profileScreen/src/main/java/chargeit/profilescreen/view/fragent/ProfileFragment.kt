package chargeit.profilescreen.view.fragent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import chargeit.core.view.CoreFragment
import chargeit.profilescreen.R
import chargeit.profilescreen.data.model.UserUiModel
import chargeit.profilescreen.databinding.FragmentProfileBinding
import chargeit.profilescreen.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : CoreFragment(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeData()
    }

    private fun observeData() {
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                initLoginUi(it)
            } else {
                initLogoutUi()
            }
        }
    }

    private fun initLoginUi(model: UserUiModel) {
        binding.emptyProfileInfoBloc.visibility = View.GONE
        binding.fullProfileInfoBloc.visibility = View.VISIBLE
        binding.logoutButton.visibility = View.VISIBLE

        binding.nameUserTextView.text = model.name
        binding.phoneUserTextView.text = model.phone.toString()
        binding.emailUserTextView.text = model.email
        binding.brandCarTextView.text = model.brand
        binding.modelCarTextView.text = model.model
        binding.connectorCarTextView.text = model.socket

        binding.logoutButton.setOnClickListener { viewModel.logout() }
    }

    private fun initLogoutUi() {
        binding.emptyProfileInfoBloc.visibility = View.VISIBLE
        binding.fullProfileInfoBloc.visibility = View.GONE
        binding.logoutButton.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkUserData()
    }

    private fun initListeners() {
        binding.registrationButton.setOnClickListener {
            viewModel.navigateToRegistrationScreen()
        }
        binding.enterButton.setOnClickListener {
            viewModel.navigateToLoginScreen()
        }
        binding.changeData.setOnClickListener {
            Toast.makeText(requireContext(), "Данные пока нельзя изменить", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment()
        const val NO_SUCH_FIELD = "Параметр не указан"
    }
}