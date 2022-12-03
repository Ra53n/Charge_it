package chargeit.station_info.ui.brief

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.Socket
import chargeit.station_info.R
import chargeit.station_info.databinding.FragmentStationInfoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale
import java.util.*


class StationInfoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentStationInfoBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var distance: Double? = null
    private var electricStationEntity: ElectricStationEntity? = null
    private var adapter = InfoSocketListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStationInfoBottomSheetBinding.bind(
            inflater.inflate(
                R.layout.fragment_station_info_bottom_sheet,
                container,
                false
            )
        )
        arguments?.let {
            distance = it.getDouble(DISTANCE_EXTRA)
            electricStationEntity = it.getParcelable(INFO_EXTRA)
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moreInfoButton.setOnClickListener {
            Toast.makeText(requireContext(), "Info button clicked!", Toast.LENGTH_SHORT).show()
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

        if (electricStationEntity != null && distance != null) {
            adapter.setData(electricStationEntity!!.listOfSockets)
            with(binding) {
                stationConnectorListRecyclerView.adapter = adapter
                distanceButton.text =
                    "$distance " + getString(chargeit.core.R.string.length_unit_km_text)
                stationAddressTextView.text = getAddressFromCoordinate(
                    electricStationEntity!!.lat,
                    electricStationEntity!!.lon
                )
            }
        } else {
            makeViewsInvisible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAddressFromCoordinate(lat: Double, lon: Double): String {
        val fullAddress = StringBuilder()
        val geocoder = Geocoder(requireContext(), Locale("RU"))
        val addresses = geocoder.getFromLocation(lat, lon, 1)

        if (addresses?.get(0)?.thoroughfare != null) fullAddress.append(addresses[0].thoroughfare)
        fullAddress.append(", ")
        if (addresses?.get(0)?.subThoroughfare != null) fullAddress.append(addresses[0].subThoroughfare)
        fullAddress.append(", ")
        if (addresses?.get(0)?.locality != null) fullAddress.append(addresses[0].locality)
        fullAddress.append("\n")
        if (addresses?.get(0)?.countryName != null) fullAddress.append(addresses[0].countryName)
        fullAddress.append(", ")
        if (addresses?.get(0)?.postalCode != null) fullAddress.append(addresses[0].postalCode)
        fullAddress.append(", ")

        return fullAddress.toString()
    }

    private fun makeViewsInvisible() {
        with(binding) {
            stationInfoTextView.text = getString(chargeit.core.R.string.no_info_about_station_text)
            moreInfoButton.visibility = View.GONE
            distanceButton.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "StationInfoBottomSheetFragment"
        const val INFO_EXTRA = "Station info"
        const val DISTANCE_EXTRA = "Distance"

        //фейковые данные для bottom sheet с краткой информацией о заправке
        const val distance = 5.7
        private val socketList = arrayListOf(
            Socket(0, chargeit.core.R.drawable.type_1_j1772, "Type 1", ""),
            Socket(1, chargeit.core.R.drawable.type_2_mannekes, "Type 2", ""),
            Socket(2, chargeit.core.R.drawable.ccs_combo_1, "CCS Combo 1", ""),
            Socket(3, chargeit.core.R.drawable.ccs_combo_2, "CCS Combo 2", ""),
            Socket(4, chargeit.core.R.drawable.chademo, "CHAdeMO", "")
        )
        val electricStationEntity = ElectricStationEntity(
            55,
            55.854517,
            37.585736,
            "",
            socketList,
            "",
            "",
            "",
            "",
            false,
            true
        )
    }

}