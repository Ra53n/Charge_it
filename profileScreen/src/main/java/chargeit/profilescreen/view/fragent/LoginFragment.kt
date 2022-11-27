package chargeit.profilescreen.view.fragent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import chargeit.core.view.CoreFragment
import chargeit.data.domain.model.State
import chargeit.profilescreen.R
import chargeit.profilescreen.data.model.LoginUiModel
import chargeit.profilescreen.databinding.LoginFragmentBinding
import chargeit.profilescreen.setEmptyError
import chargeit.profilescreen.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : CoreFragment(R.layout.login_fragment) {

    val loginViewModel: LoginViewModel by viewModel()

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeData()
    }

    private fun observeData() {
        loginViewModel.loginLiveData.observe(viewLifecycleOwner) {
            when (it) {
                State.Success -> {
                    Toast.makeText(requireContext(), "Вы успешно вошли", Toast.LENGTH_SHORT)
                        .show()
                    this.findNavController().navigateUp()
                }
                State.Error -> {
                    Toast.makeText(requireContext(), "Ошибка авторизации", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initUi() {
        binding.loginButton.setOnClickListener {
            if (validateFields()) {
                loginViewModel.login(
                    LoginUiModel(
                        binding.nameEditText.text.toString(),
                        binding.phoneEditText.text.toString()
                    )
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        var validationResult = true
        when {
            binding.nameEditText.text.isNullOrEmpty() -> {
                binding.nameEditText.setEmptyError()
                validationResult = false
            }
            binding.phoneEditText.text.isNullOrEmpty() -> {
                binding.phoneEditText.setEmptyError()
                validationResult = false
            }
        }
        return validationResult
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileRegistrationFragment()
    }
}