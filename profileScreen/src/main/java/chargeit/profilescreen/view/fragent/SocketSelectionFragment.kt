package chargeit.profilescreen.view.fragent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chargeit.core.view.CoreFragment
import chargeit.profilescreen.R
import chargeit.profilescreen.databinding.SocketSelectionFragmentBinding
import chargeit.profilescreen.view.adapter.SocketSelectionAdapter
import chargeit.profilescreen.viewmodel.SocketSelectionViewModel
import com.google.gson.Gson

class SocketSelectionFragment : CoreFragment(R.layout.socket_selection_fragment) {

    override val viewModel: SocketSelectionViewModel by viewModels()

    private var adapter: SocketSelectionAdapter? = null

    private var _binding: SocketSelectionFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SocketSelectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.confirmButton.setOnClickListener {
            adapter?.saveSockets()
        }
        binding.unselectButton.setOnClickListener {
            adapter?.unselectSockets()
        }
        binding.searchEditText.addTextChangedListener { text ->
            viewModel.filterSockets(text.toString())
        }
    }

    private fun initAdapter() {
        viewModel.socketsLiveData.observe(viewLifecycleOwner) {
            adapter = SocketSelectionAdapter {
                val bundle = Bundle().apply { putString("SOCKET_LIST", Gson().toJson(it)) }
                setFragmentResult(
                    "SOCKET_LIST_RESULT",
                    bundle
                )
                findNavController().navigateUp()
            }.apply { setItems(it) }
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.filteredSocketsLiveData.observe(viewLifecycleOwner) {
            adapter?.filterItems(it)
        }
        viewModel.requireSocketsList()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}