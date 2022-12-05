package chargeit.station_info.ui.full

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import chargeit.core.view.CoreFragment
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.station_info.R
import chargeit.station_info.databinding.FragmentFullStationInfoBinding
import chargeit.station_info.databinding.FragmentStationInfoBottomSheetBinding
import chargeit.station_info.ui.brief.InfoSocketListAdapter
import chargeit.station_info.ui.brief.StationInfoBottomSheetFragment
import chargeit.station_info.ui.brief.StationInfoBottomSheetFragment.Companion.INFO_EXTRA
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.full_info_station_socket_list_item.view.*

class FullStationInfoFragment : CoreFragment(R.layout.fragment_full_station_info) {

    private var _binding: FragmentFullStationInfoBinding? = null
    private val binding get() = _binding!!
    private var electricStationEntity: ElectricStationEntity? = null
    private var stationAddress = ""
    private var adapter: SocketListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SocketListAdapter(object : OnItemClickListener{
            override fun onItemClick(view: View, socket: Socket) {
                if (view.socket_status_text_view.text == getString(chargeit.core.R.string.busy_socket_status_text)){
                    showDialog(
                        view,
                        socket,
                        "Завершить зарядку автомобиля?",
                        getString(chargeit.core.R.string.free_socket_status_text),
                        resources.getDrawable(chargeit.core.R.drawable.free_connector_layout_shape)
                    )
                } else{
                    showDialog(
                        view,
                        socket,
                        "Начать зарядку автомобиля?",
                        getString(chargeit.core.R.string.busy_socket_status_text),
                        resources.getDrawable(chargeit.core.R.drawable.busy_connector_layout_shape)
                    )
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFullStationInfoBinding.bind(inflater.inflate(R.layout.fragment_full_station_info, container, false))
        arguments?.let {
            stationAddress = it.getString(ADDRESS_EXTRA,"")
            electricStationEntity = it.getParcelable(INFO_EXTRA)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        electricStationEntity?.let {
            adapter!!.setData(it.listOfSockets)
            with(binding) {
                stationAddressTextView.text = stationAddress
                workTimeTextView.text = it.workTime

                if (it.paidCost) costTextView.text = getString(chargeit.core.R.string.paid_station_text)
                else costTextView.text = getString(chargeit.core.R.string.free_station_text)

                stationInfoTextView.text = it.additionalInfo
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = it.titleStation
                stationSocketListRecyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(view: View, socket: Socket, message: String, status: String, shape: Drawable) {
        val builder = MaterialAlertDialogBuilder(requireContext())

        with(builder) {
            setTitle("Разъём ${socket.title} / ${socket.description}")
            setMessage(message)
            setPositiveButton(getString(chargeit.core.R.string.ok_button_text)) { _, _ ->
                view.socket_status_text_view.text = status
                view.connector_status_constraint_layout.setBackground(shape)
            }
            setNegativeButton(getString(chargeit.core.R.string.cancel_button_text)) { _, _ ->}
            show()
        }
    }

    companion object{
        const val ADDRESS_EXTRA = "Address"
        const val INFO_EXTRA = "Station info"
    }
}