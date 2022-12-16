package chargeit.station_info.presentation.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import chargeit.core.utils.EMPTY
import chargeit.core.view.CoreFragment
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.data.domain.model.SocketEntity
import chargeit.station_info.R
import chargeit.station_info.databinding.FragmentFullStationInfoBinding
import chargeit.station_info.presentation.view.adapter.SocketListAdapter
import chargeit.station_info.presentation.view.utils.OnItemClickListener
import chargeit.station_info.presentation.viewmodel.FullStationInfoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.full_info_station_socket_list_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FullStationInfoFragment : CoreFragment(R.layout.fragment_full_station_info) {

    private var _binding: FragmentFullStationInfoBinding? = null
    private val binding get() = _binding!!
    private var electricStationEntity: ElectricStationEntity? = null
    private var stationAddress = String.EMPTY
    private var id: Int? = null
    private val fullStationInfoViewModel: FullStationInfoViewModel by viewModel()
    private val adapter by lazy {
        SocketListAdapter(object : OnItemClickListener {
            override fun onItemClick(socket: SocketEntity) {
                val bundle = Bundle().apply {
                    id?.let { putInt(SOCKET_EXTRA, it) }
                }
                fullStationInfoViewModel.navigateToSocketInfoScreen(bundle)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFullStationInfoBinding.bind(
            inflater.inflate(
                R.layout.fragment_full_station_info,
                container,
                false
            )
        )
        arguments?.let {
            stationAddress = it.getString(ADDRESS_EXTRA, String.EMPTY)
            id = it.getInt(INFO_EXTRA)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id?.let {
            fullStationInfoViewModel.getElectricStationInfo(it)
        }
        observeData()
    }

    private fun observeData() {
        fullStationInfoViewModel.electricStationLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it[0].listOfSockets)
            with(binding) {
                stationAddressTextView.text = stationAddress
                workTimeTextView.text = it[0].workTime

                if (it[0].paidCost) costTextView.text =
                    getString(chargeit.core.R.string.paid_station_text)
                else costTextView.text = getString(chargeit.core.R.string.free_station_text)

                stationInfoTextView.text = it[0].additionalInfo
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = it[0].titleStation
                stationSocketListRecyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ADDRESS_EXTRA = "Address"
        const val INFO_EXTRA = "Station info"
        const val SOCKET_EXTRA = "Socket"
    }
}