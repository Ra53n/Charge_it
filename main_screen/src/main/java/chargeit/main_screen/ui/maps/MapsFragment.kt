package chargeit.main_screen.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import chargeit.core.view.CoreFragment
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.messages.AppMessage
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.ui.filters.FiltersFragment
import chargeit.main_screen.utils.MapHelper
import chargeit.main_screen.utils.PermissionHelper
import chargeit.station_info.presentation.view.fragment.StationInfoBottomSheetFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : CoreFragment(R.layout.fragment_maps) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    private val mapsFragmentViewModel: MapsFragmentViewModel by viewModel()
    private val mapHelper: MapHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionHelper = PermissionHelper(requireActivity(), this)
        initMapsFragmentViewModel()
        mapView = view.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            map = googleMap
            mapHelper.initMap(map, mapsFragmentViewModel)
        }
        permissionHelper.startAccessToLocation(mapsFragmentViewModel)
        initSearch()
        initButtons()
        initFiltersFragment()
    }

    private fun initFiltersFragment() {
        requireActivity().supportFragmentManager
            .setFragmentResultListener(REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
                val result = bundle.getParcelable<FiltersMessage.ChargeFilters>(RESULT_BUNDLE_KEY)
                result?.let { chargeFilters ->
                    mapsFragmentViewModel.requestChargeStations(chargeFilters)
                }
            }
    }

    private fun initMapsFragmentViewModel() {
        with(mapsFragmentViewModel) {
            messagesLiveData.observe(viewLifecycleOwner) { message ->
                processMessages(message)
            }
            locationLiveData.observe(viewLifecycleOwner) { locationMarker ->
                mapHelper.changeLocationMarker(locationMarker.markerOptions)
            }
        }
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mapsFragmentViewModel.requestAddressSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun initButtons() {
        with(mapsFragmentViewModel) {
            binding.filterScreenButton.setOnClickListener { onFilterScreenButtonClick() }
            binding.zoomInButton.setOnClickListener {
                onZoomInButtonClick(map.cameraPosition.target, map.cameraPosition.zoom)
            }
            binding.zoomOutButton.setOnClickListener {
                onZoomOutButtonClick(map.cameraPosition.target, map.cameraPosition.zoom)
            }
            binding.deviceLocationButton.setOnClickListener { onDeviceLocationButtonClick() }
        }
    }

    private fun processMessages(appMessage: AppMessage) {
        hideKeyboard(requireActivity())
        binding.search.clearFocus()
        with(appMessage) {
            when (this) {
                is AppMessage.StationInfo -> openStationInfo(entity, distance)
                is AppMessage.Filters -> openFilters()
                is AppMessage.ChargeStationMarkers -> mapHelper.changeStationMarkers(clusterItems)
                is AppMessage.AddressMarker -> mapHelper.changeAddressMarker(markerOptions)
                is AppMessage.MoveCamera -> mapHelper.moveCamera(map, animated, location, zoomLevel)
                is AppMessage.InfoDialog -> showDialog(requireContext(), title, message)
                is AppMessage.InfoSnackBar -> makeSnackbar(
                    view = binding.root,
                    text = text,
                    anchor = binding.anchor
                )
                is AppMessage.InfoToast -> Toast.makeText(context, text, length).show()
            }
        }
    }

    private fun openFilters() {
        val filtersFragment = FiltersFragment.newInstance()
        filtersFragment.show(requireActivity().supportFragmentManager, FiltersFragment.TAG)
    }

    private fun openStationInfo(entity: ElectricStationEntity, distance: Double) {
        val bundle = Bundle().apply {
            putDouble(StationInfoBottomSheetFragment.DISTANCE_EXTRA, distance)
            putParcelable(StationInfoBottomSheetFragment.INFO_EXTRA, entity)
        }
        mapsFragmentViewModel.navigateToStationInfoBottomSheet(bundle)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapsFragmentViewModel.stopLocationUpdates()
        mapsFragmentViewModel.saveCameraState(map.cameraPosition.target, map.cameraPosition.zoom)
        mapView.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        private const val REQUEST_KEY = "REQUEST_KEY"
        private const val RESULT_BUNDLE_KEY = "RESULT_BUNDLE_KEY"

        @JvmStatic
        fun newInstance() = MapsFragment()
    }

}
