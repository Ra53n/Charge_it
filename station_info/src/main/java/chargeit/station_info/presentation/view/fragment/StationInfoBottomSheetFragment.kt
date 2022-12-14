package chargeit.station_info.presentation.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.data.domain.model.SocketEntity
import chargeit.station_info.R
import chargeit.station_info.databinding.FragmentStationInfoBottomSheetBinding
import chargeit.station_info.presentation.view.adapter.InfoSocketListAdapter
import chargeit.station_info.presentation.viewmodel.StationInfoBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class StationInfoBottomSheetFragment : BottomSheetDialogFragment() {

    private val stationInfoBottomSheetViewModel: StationInfoBottomSheetViewModel by viewModel()
    private var _binding: FragmentStationInfoBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var distance: Double? = null
    private var electricStationEntity: ElectricStationEntity? = null
    private var stationAddress: String? = null
    private var adapter = InfoSocketListAdapter()
    private var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStationInfoBottomSheetBinding.bind(
            inflater.inflate(
                R.layout.fragment_station_info_bottom_sheet,
                container,
                false
            )
        )
        arguments?.let {
            distance = it.getDouble(DISTANCE_EXTRA, 0.0)
            id = it.getInt(INFO_EXTRA, 0)
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id?.let { stationInfoBottomSheetViewModel.getElectricStationInfo(it) }

        observeData()
        initViews()
    }

    private fun observeData() {
        stationInfoBottomSheetViewModel.electricStationLiveData.observe(viewLifecycleOwner) {
            stationAddress = it.let {
                stationInfoBottomSheetViewModel.getAddressFromCoordinate(
                    it[0].lat,
                    it[0].lon,
                    requireContext()
                )
            }
            adapter.setData(it[0].listOfSockets)
            electricStationEntity = it[0]
            with(binding) {
                stationConnectorListRecyclerView.adapter = adapter
                distanceButton.text = resources.getString(chargeit.core.R.string.length_unit_km_text, distance.toString())
                stationAddressTextView.text = stationAddress
            }
        }
    }

    private fun initViews() {
        binding.moreInfoButton.setOnClickListener {
            findNavController().navigateUp()

            val bundle = Bundle().apply {
                putString(FullStationInfoFragment.ADDRESS_EXTRA, stationAddress)
                id?.let {
                    putInt(
                    INFO_EXTRA,
                    it
                )}
            }
            stationInfoBottomSheetViewModel.navigateToFullStationInfo(bundle)
        }

        binding.distanceButton.setOnClickListener {
            electricStationEntity?.let { entity ->
                val uri = getString(R.string.uri, entity.lat.toString(), entity.lon.toString())
                val mapIntentUri = Uri.parse(uri)
                val message = getString(R.string.no_suitable_application_found)
                val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
                mapIntent.setPackage(getString(R.string.package_name))
                if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.closeSignImageView.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeViewsInvisible() {
        with(binding) {
            stationInfoTextView.text = getString(chargeit.core.R.string.no_info_about_station_text)
            moreInfoButton.visibility = View.GONE
            distanceButton.visibility = View.GONE
        }
    }

    companion object {
        const val INFO_EXTRA = "Station info"
        const val DISTANCE_EXTRA = "Distance"
    }

}