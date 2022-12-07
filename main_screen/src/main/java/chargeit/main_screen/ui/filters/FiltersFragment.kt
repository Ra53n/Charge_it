package chargeit.main_screen.ui.filters

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import chargeit.main_screen.databinding.FragmentFiltersBinding
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.utils.setupFullHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FiltersFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FiltersFragmentViewModel by viewModel()
    private val adapter: FiltersFragmentAdapter by lazy {
        FiltersFragmentAdapter(
            onSwitchChecked = { position, adapter, isChecked ->
                adapter.switchFilter(position, isChecked)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { currentDialog ->
            if (currentDialog != null) {
                val designBottomSheet = com.google.android.material.R.id.design_bottom_sheet
                val bottomSheetDialog = currentDialog as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(designBottomSheet)
                if (parentLayout != null) {
                    BottomSheetBehavior.from(parentLayout).apply {
                        skipCollapsed = true
                        setupFullHeight(parentLayout)
                        state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.filtersList.adapter = adapter
        binding.filtersMenu.setOnClickListener { viewModel.onMenuClick() }
        viewModel.requestFilters()
        println("VVV Hash: ${viewModel.hashCode()}")
    }

    private fun initViewModel() {
        viewModel.filtersLiveData.observe(viewLifecycleOwner) { message ->
            when (message) {
                is FiltersMessage.ChargeFilters -> adapter.setFilters(message)
                is FiltersMessage.SwitchAllOff -> adapter.switchAllOff()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val bundle = bundleOf(RESULT_BUNDLE_KEY to adapter.getFilters())
        requireActivity().supportFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_FRAGMENT"
        private const val REQUEST_KEY = "REQUEST_KEY"
        private const val RESULT_BUNDLE_KEY = "RESULT_BUNDLE_KEY"

        @JvmStatic
        fun newInstance() = FiltersFragment()
    }
}