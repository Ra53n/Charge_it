package chargeit.main_screen.ui

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import chargeit.core.utils.EMPTY
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.charge_stations.ChargeStation
import chargeit.main_screen.domain.charge_stations.ChargeStationsState
import chargeit.main_screen.domain.device_location.DeviceLocation
import chargeit.main_screen.domain.device_location.DeviceLocationError
import chargeit.main_screen.domain.device_location.DeviceLocationEvent
import chargeit.main_screen.domain.device_location.DeviceLocationState
import chargeit.main_screen.domain.search_addresses.SearchAddress
import chargeit.main_screen.domain.search_addresses.SearchAddressError
import chargeit.main_screen.domain.search_addresses.SearchAddressState
import chargeit.main_screen.settings.*
import chargeit.main_screen.utils.isAtLeastOneGranted
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : CoreFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val mapsFragmentViewModel: MapsFragmentViewModel by viewModel()
    private var currentAddressMarker: Marker? = null
    private var currentDeviceLocationMarker: Marker? = null
    private var locationShowRequestFlag = false
    private var googlePlayServicesNotPresentErrorFlag = false
    private var locationIsNotAvailableErrorFlag = false
    private val defaultLocation = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE)
    private var currentDeviceLocation = defaultLocation

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { status ->
            checkPermissionRequestResult(status)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
        startAccessToLocation()
        initSearch()
        initButtons()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initViewModel()
        initMap()
        mapsFragmentViewModel.requestChargeStations()
    }

    private fun initViewModel() {
        with(mapsFragmentViewModel) {
            deviceLocationStateLD.observe(viewLifecycleOwner) { deviceLocationState ->
                handleDeviceLocationState(deviceLocationState)
            }
            searchAddressStateLD.observe(viewLifecycleOwner) { searchAddressState ->
                handleAddressState(searchAddressState)
            }
            chargeStationsStateLD.observe(viewLifecycleOwner) { chargeStationsState ->
                handleChargeStationsState(chargeStationsState)
            }
        }
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(requireActivity())
                mapsFragmentViewModel.checkQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun initButtons() {
        with(binding) {
            filterScreenButton.setOnClickListener { filterScreenButtonClick() }
            deviceCoordinatesButton.setOnClickListener { deviceCoordinatesButtonClick() }
        }
    }

    private fun initMap() {
        googleMapInterfaceConfig()
        setMarkersBehavior()
        showInitialDeviceLocation()
    }

    private fun shouldShowAtLeastOneRationale() =
        shouldShowRequestPermissionRationale(COARSE_PERMISSION)
                || shouldShowRequestPermissionRationale(FINE_PERMISSION)

    private fun checkPermissions() {
        if (!isAtLeastOneGranted(requireContext())) {
            if (shouldShowAtLeastOneRationale()) {
                showRationaleDialog()
            } else {
                showNotGrantedNoAskDialog()
            }
        }
    }

    private fun checkErrorFlags() {
        if (googlePlayServicesNotPresentErrorFlag) {
            showNoPlayServicesDialog()
        }
        if (locationIsNotAvailableErrorFlag) {
            makeSnackbar(
                view = binding.root,
                text = getString(R.string.location_is_not_available_error)
            )
        }
    }

    private fun startAccessToLocation() {
        if (isAtLeastOneGranted(requireContext())) {
            startLocationUpdates()
        } else {
            permissionRequest.launch(arrayOf(COARSE_PERMISSION, FINE_PERMISSION))
        }
    }

    private fun checkPermissionRequestResult(status: Map<String, Boolean>) {
        val noRationale = !shouldShowAtLeastOneRationale()
        val isGranted = (status[COARSE_PERMISSION] == true || status[FINE_PERMISSION] == true)
        when {
            isGranted -> startLocationUpdates()
            noRationale -> showNotGrantedNoAskDialog()
            else -> showRationaleDialog()
        }
    }

    private fun startLocationUpdates() {
        mapsFragmentViewModel.startLocationUpdates()
        locationShowRequestFlag = true
    }

    private fun googleMapInterfaceConfig() {
        with(map) {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isRotateGesturesEnabled = false
        }
    }

    private fun filterScreenButtonClick() {
        BottomSheetBehavior.from(binding.bottomSheetIncluded.bottomSheetRoot).state =
            BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun deviceCoordinatesButtonClick() {
        checkPermissions()
        checkErrorFlags()
        if (currentDeviceLocation != defaultLocation) {
            showCurrentDeviceLocation()
        }
    }

    private fun showInitialDeviceLocation() {
        moveCamera(currentDeviceLocation, DEFAULT_PLACE_ZOOM_LEVEL, false)
        locationShowRequestFlag = true
    }

    private fun showCurrentDeviceLocation() {
        moveCamera(currentDeviceLocation, DEVICE_LOCATION_ZOOM_LEVEL, true)
        locationShowRequestFlag = false

    }

    private fun setMarkersBehavior() {
        map.setOnMarkerClickListener { marker ->
            val title = marker.title ?: getString(R.string.no_title_message)
            makeSnackbar(view = binding.root, text = title)
            true
        }
    }

    private fun moveCamera(location: LatLng, zoomLevel: Float, isAnimated: Boolean) {
        val cameraProperties = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
        if (isAnimated) {
            map.animateCamera(cameraProperties)
        } else {
            map.moveCamera(cameraProperties)
        }
    }

    private fun showLocationMarker(markerOptions: MarkerOptions) {
        currentDeviceLocationMarker?.remove()
        currentDeviceLocationMarker = map.addMarker(markerOptions)
    }

    private fun showAddressMarker(markerOptions: MarkerOptions) {
        currentAddressMarker?.remove()
        currentAddressMarker = map.addMarker(markerOptions)
    }

    private fun showChargeStationMarker(markerOptions: MarkerOptions) {
        map.addMarker(markerOptions)
    }

    private fun showAddress(address: Address, zoomLevel: Float, isAnimated: Boolean) {
        moveCamera(LatLng(address.latitude, address.longitude), zoomLevel, isAnimated)
    }

    private fun handleDeviceLocationState(deviceLocationState: DeviceLocationState) {
        when (deviceLocationState) {
            is DeviceLocationState.Success -> showLocation(deviceLocationState.deviceLocation)
            is DeviceLocationState.Error -> processLocationErrors(deviceLocationState.deviceLocationError)
            is DeviceLocationState.Event -> processLocationEvents(deviceLocationState.deviceLocationEvent)
            is DeviceLocationState.Loading -> showLocationLoadingStatus()
        }
    }

    private fun showLocation(deviceLocation: DeviceLocation) {
        currentDeviceLocation = deviceLocation.markerOptions.position
        showLocationMarker(deviceLocation.markerOptions)
        if (locationShowRequestFlag) {
            showCurrentDeviceLocation()
        }
    }

    private fun processLocationErrors(deviceLocationError: DeviceLocationError) {
        when (deviceLocationError.errorID) {
            PERMISSION_ERROR_ID -> checkPermissions()
            GOOGLE_PLAY_SERVICES_NOT_PRESENT_ERROR_ID -> processNoPlayServicesError()
            LOCATION_IS_NOT_AVAILABLE_ERROR_ID -> showLocationIsNotAvailableDialog()
        }
    }

    private fun processNoPlayServicesError() {
        googlePlayServicesNotPresentErrorFlag = true
        showNoPlayServicesDialog()
    }

    private fun processLocationEvents(deviceLocationEvent: DeviceLocationEvent) {
        if (deviceLocationEvent.eventID == LOCATION_IS_AVAILABLE_EVENT_ID) {
            locationIsNotAvailableErrorFlag = false
            makeSnackbar(
                view = binding.root,
                text = deviceLocationEvent.message
            )
        }
    }

    private fun showLocationLoadingStatus() {}

    private fun handleAddressState(searchAddressState: SearchAddressState) {
        when (searchAddressState) {
            is SearchAddressState.Success -> showFoundAddresses(searchAddressState.searchAddress)
            is SearchAddressState.Error -> processAddressStateError(searchAddressState.searchAddressError)
            is SearchAddressState.Loading -> showAddressStateLoadingStatus()
        }
    }

    private fun showFoundAddresses(searchAddress: SearchAddress) {
        val address = searchAddress.address
        showAddressMarker(searchAddress.markerOptions)
        showAddress(address, ADDRESS_SEARCH_ZOOM_LEVEL, true)
    }

    private fun processAddressStateError(searchAddressError: SearchAddressError) {
        makeSnackbar(
            view = binding.root,
            text = searchAddressError.message ?: String.EMPTY
        )
    }

    private fun showAddressStateLoadingStatus() {}

    private fun handleChargeStationsState(state: ChargeStationsState) {
        when (state) {
            is ChargeStationsState.Success -> showChargeStationList(state.chargeStations)
            is ChargeStationsState.Error -> processChargeStationError(state.chargeStationError)
            is ChargeStationsState.Loading -> showChargeStationsLoadingStatus()
        }
    }

    private fun showChargeStationList(chargeStations: List<ChargeStation>) {
        chargeStations.forEach { chargeStation ->
            showChargeStationMarker(chargeStation.markerOptions)
        }
    }

    private fun processChargeStationError(error: Throwable) {}

    private fun showChargeStationsLoadingStatus() {}

    private fun showRationaleDialog() {
        showDialog(
            title = getString(R.string.rationale_title),
            message = getString(R.string.rationale_message),
            positiveButtonText = getString(R.string.dialogs_positive_button)
        )
    }

    private fun showNotGrantedNoAskDialog() {
        showDialog(
            title = getString(R.string.not_granted_no_ask_title),
            message = getString(R.string.not_granted_no_ask_message),
            positiveButtonText = getString(R.string.dialogs_positive_button)
        )
    }

    private fun showLocationIsNotAvailableDialog() {
        locationIsNotAvailableErrorFlag = true
        showDialog(
            title = getString(R.string.location_not_available_title),
            message = getString(R.string.location_not_available_message),
            positiveButtonText = getString(R.string.dialogs_positive_button)
        )
    }

    private fun showNoPlayServicesDialog() {
        showDialog(
            title = getString(R.string.no_play_services_title),
            message = getString(R.string.no_play_services_message),
            positiveButtonText = getString(R.string.dialogs_positive_button)
        )
    }

    private fun showDialog(title: String, message: String, positiveButtonText: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText)
            { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ADDRESS_SEARCH_ZOOM_LEVEL = 10.0f
        private const val DEVICE_LOCATION_ZOOM_LEVEL = 15f
        private const val DEFAULT_PLACE_ZOOM_LEVEL = 10.0f
        private const val DEFAULT_PLACE_LATITUDE = 55.751513
        private const val DEFAULT_PLACE_LONGITUDE = 37.616655

        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}