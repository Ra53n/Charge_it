package chargeit.main_screen.ui.filters

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowManager
import chargeit.main_screen.databinding.FragmentFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FiltersFragment : BottomSheetDialogFragment(), OnShowListener, OnClickListener {

    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FiltersFragmentViewModel by viewModel()
    private val adapter = FiltersFragmentAdapter(
        onSwitchChecked = { position, adapter, isChecked ->
            adapter.switchFilter(position, isChecked)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onClick(v: View?) {
        adapter.switchAllOff()
    }

    private fun initViewModel() {
        viewModel.filtersLiveData.observe(viewLifecycleOwner) { filters ->
            adapter.setFilters(filters)
        }
    }

    override fun onShow(dialog: DialogInterface?) {
        if (dialog != null) {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (parentLayout != null) {
                val behaviour = BottomSheetBehavior.from(parentLayout)
                behaviour.skipCollapsed = true
                setupFullHeight(parentLayout)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener(this)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.filtersList.adapter = adapter
        binding.filtersMenu.setOnClickListener(this)
        viewModel.requestFilters()
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.saveFilters(adapter.getFilters())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_FRAGMENT"

        @JvmStatic
        fun newInstance() = FiltersFragment()
    }
}