package chargeit.profilescreen.view.fragent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import chargeit.core.view.CoreFragment
import chargeit.data.domain.model.Socket
import chargeit.data.domain.model.State
import chargeit.profilescreen.R
import chargeit.profilescreen.data.model.UserUiModel
import chargeit.profilescreen.databinding.ProfileRegistrationFragmentBinding
import chargeit.profilescreen.setEmptyError
import chargeit.profilescreen.viewmodel.ProfileRegistrationViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileRegistrationFragment : CoreFragment(R.layout.profile_registration_fragment) {

    override val viewModel: ProfileRegistrationViewModel by viewModel()

    private val carBrandAdapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            R.layout.drop_down_item
        )
    }

    private val carModelAdapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            R.layout.drop_down_item
        )
    }


    private var _binding: ProfileRegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ProfileRegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        initUi()
    }

    private fun observeData() {
        viewModel.carBrandsLiveData.observe(viewLifecycleOwner) {
            carBrandAdapter.clear()
            carBrandAdapter.addAll(it)
        }
        viewModel.carModelLiveData.observe(viewLifecycleOwner) {
            carModelAdapter.clear()
            carModelAdapter.addAll(it)
        }

        viewModel.socketsLiveData.observe(viewLifecycleOwner) {
            binding.socketInputEditText.setText(it)
        }

        viewModel.registrationLiveData.observe(viewLifecycleOwner) {
            when (it) {
                State.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Вы успешно зарегистрированы!",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.findNavController().navigateUp()
                }
                State.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initViews() {
        binding.carBrandTextView.setAdapter(carBrandAdapter)
        binding.carBrandTextView.setOnItemClickListener { adapterView, _, item, _ ->
            viewModel.loadCarModels(adapterView.adapter.getItem(item) as String)
        }
        binding.carModelTextView.setAdapter(carModelAdapter)
        binding.registerButton.setOnClickListener {
            if (validateFields()) {
                registerUser()
            }
        }
        binding.socketInputEditText.setOnClickListener {
            viewModel.navigateToSocketSelection()
        }
        setFragmentResultListener("SOCKET_LIST_RESULT") { _, bundle ->
            val socketList = Gson().fromJson(
                bundle.getString("SOCKET_LIST", ""), Array<Socket>::class.java
            ).toList()
            viewModel.setSockets(socketList)
        }
    }

    private fun registerUser() {
        viewModel.saveUser(
            UserUiModel(
                binding.nameEditText.text.toString(),
                binding.phoneEditText.text.toString().toLong(),
                binding.emailEditText.text.toString(),
                binding.carBrandTextView.text.toString(),
                binding.carModelTextView.text.toString(),
                binding.socketInputEditText.text.toString()
            )
        )
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
            binding.emailEditText.text.isNullOrEmpty() -> {
                binding.emailEditText.setEmptyError()
                validationResult = false
            }
        }
        return validationResult
    }

    private fun initUi() {
        viewModel.loadCarBrands()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileRegistrationFragment()
    }
}