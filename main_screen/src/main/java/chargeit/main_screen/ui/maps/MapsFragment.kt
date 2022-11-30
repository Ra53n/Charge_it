package chargeit.main_screen.ui.maps

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.data.ChargeStationIconRenderer
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.charge_stations.ChargeStation
import chargeit.main_screen.domain.charge_stations.ChargeStationsState
import chargeit.main_screen.domain.device_location.DeviceLocation
import chargeit.main_screen.domain.device_location.DeviceLocationState
import chargeit.main_screen.domain.filters.ChargeFilter
import chargeit.main_screen.domain.message.AppMessage
import chargeit.main_screen.domain.search_addresses.SearchAddress
import chargeit.main_screen.domain.search_addresses.SearchAddressState
import chargeit.main_screen.settings.*
import chargeit.main_screen.ui.filters.FiltersFragment
import chargeit.main_screen.ui.filters.FiltersFragmentViewModel
import chargeit.main_screen.utils.isAtLeastOnePermissionGranted
import chargeit.station_info.ui.StationInfoBottomSheetFragment
import chargeit.station_info.ui.StationInfoBottomSheetFragment.Companion.electricStationEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : CoreFragment(R.layout.fragment_maps), OnMapReadyCallback,
    ActivityResultCallback<Map<String, @JvmSuppressWildcards Boolean>>, OnMarkerClickListener,
    ClusterManager.OnClusterItemClickListener<MarkerClusterItem>,
    ClusterManager.OnClusterClickListener<MarkerClusterItem> {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val mapsFragmentViewModel: MapsFragmentViewModel by viewModel()
    private val filtersFragmentViewModel: FiltersFragmentViewModel by viewModel()
    private var locationShowRequestFlag = false
    private var googlePlayServicesNotPresentErrorFlag = false
    private var locationIsNotAvailableErrorFlag = false
    private val defaultLocation = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE)
    private var currentDeviceLocation = defaultLocation
    private lateinit var clusterManager: ClusterManager<MarkerClusterItem>
    private lateinit var locationMarkerCollection: MarkerManager.Collection
    private lateinit var addressMarkerCollection: MarkerManager.Collection
    private val filtersFragment = FiltersFragment.newInstance()

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), this)

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
        initMap()
        initClustering()
        setMapListeners()
        initMapsFragmentViewModel()
        initFiltersFragmentViewModel()
        mapsFragmentViewModel.requestChargeStations()
    }

    override fun onActivityResult(status: Map<String, Boolean>) {
        val noRationale = shouldShowAtLeastOneRationale().not()
        val isGranted = (status[COARSE_PERMISSION] == true || status[FINE_PERMISSION] == true)
        when {
            isGranted -> startLocationUpdates()
            noRationale -> showNotGrantedNoAskDialog()
            else -> showRationaleDialog()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val title = marker.title ?: getString(R.string.no_title_message)
        hideKeyboard(requireActivity())
        makeSnackbar(binding.root, title)
        return true
    }

    override fun onClusterClick(cluster: Cluster<MarkerClusterItem>?): Boolean {
        if (cluster != null) {
            val currentZoom = map.cameraPosition.zoom
            val position = cluster.position
            moveCamera(position, currentZoom + ZOOM_INCREMENT, true)
        }
        return true
    }

    override fun onClusterItemClick(item: MarkerClusterItem?): Boolean {
        if (item != null) {
            hideKeyboard(requireActivity())
            val stationInfoBottomSheetFragment = StationInfoBottomSheetFragment()
            val bundle = Bundle().apply {
                putDouble(
                    StationInfoBottomSheetFragment.DISTANCE_EXTRA,
                    StationInfoBottomSheetFragment.distance
                )
                putParcelable(StationInfoBottomSheetFragment.INFO_EXTRA, electricStationEntity)
            }
            stationInfoBottomSheetFragment.arguments = bundle
            stationInfoBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                StationInfoBottomSheetFragment.TAG
            )
        }
        return true
    }

    private fun initClustering() {
        clusterManager = ClusterManager(context, map)
        locationMarkerCollection = clusterManager.markerManager.newCollection()
        addressMarkerCollection = clusterManager.markerManager.newCollection()
        clusterManager.renderer = ChargeStationIconRenderer(requireContext(), map, clusterManager)
    }

    private fun setMapListeners() {
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemClickListener(this)
        clusterManager.setOnClusterClickListener(this)
        locationMarkerCollection.setOnMarkerClickListener(this)
        addressMarkerCollection.setOnMarkerClickListener(this)
    }

    private fun initMapsFragmentViewModel() {
        with(mapsFragmentViewModel) {
            deviceLocationStateLD.observe(viewLifecycleOwner) { locationState ->
                handleLocationState(locationState)
            }
            searchAddressStateLD.observe(viewLifecycleOwner) { addressState ->
                handleAddressState(addressState)
            }
            chargeStationsStateLD.observe(viewLifecycleOwner) { stationsState ->
                handleChargeStationsState(stationsState)
            }
        }
    }

    private fun initFiltersFragmentViewModel() {
        filtersFragmentViewModel.mapFiltersLiveData.observe(viewLifecycleOwner) { filters ->
            applyFilters(filters)
        }
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(requireActivity())
                binding.search.clearFocus()
                mapsFragmentViewModel.checkQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun initButtons() {
        with(binding) {
            filterScreenButton.setOnClickListener { filterScreenButtonClick() }
            deviceLocationButton.setOnClickListener { deviceCoordinatesButtonClick() }
        }
    }

    private fun initMap() {
        googleMapInterfaceConfig()
        showInitialDeviceLocation()
    }

    private fun shouldShowAtLeastOneRationale() =
        shouldShowRequestPermissionRationale(COARSE_PERMISSION)
                || shouldShowRequestPermissionRationale(FINE_PERMISSION)

    private fun checkPermissions() {
        if (isAtLeastOnePermissionGranted(requireContext()).not()) {
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
            makeSnackbar(binding.root, getString(R.string.message_location_is_not_available))
        }
    }

    private fun startAccessToLocation() {
        if (isAtLeastOnePermissionGranted(requireContext())) {
            startLocationUpdates()
        } else {
            permissionRequest.launch(arrayOf(COARSE_PERMISSION, FINE_PERMISSION))
        }
    }

    private fun startLocationUpdates() {
        mapsFragmentViewModel.startLocationUpdates()
        locationShowRequestFlag = true
    }

    private fun googleMapInterfaceConfig() {
        with(map.uiSettings) {
            isMyLocationButtonEnabled = false
            isRotateGesturesEnabled = false
            isMapToolbarEnabled = false
            isTiltGesturesEnabled = false
        }
    }

    private fun filterScreenButtonClick() {
        filtersFragment.show(requireActivity().supportFragmentManager, FiltersFragment.TAG)
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

    private fun moveCamera(location: LatLng, zoomLevel: Float, isAnimated: Boolean) {
        val cameraProperties = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
        if (isAnimated) {
            map.animateCamera(cameraProperties)
        } else {
            map.moveCamera(cameraProperties)
        }
    }

    private fun showLocationMarker(markerOptions: MarkerOptions) {
        locationMarkerCollection.clear()
        locationMarkerCollection.addMarker(markerOptions)
    }

    private fun showAddressMarker(markerOptions: MarkerOptions) {
        addressMarkerCollection.clear()
        addressMarkerCollection.addMarker(markerOptions)
    }

    private fun showChargeStationMarker(chargeStation: ChargeStation) {
        clusterManager.addItem(chargeStation.clusterItem)
    }

    private fun showAddress(address: Address, zoomLevel: Float, isAnimated: Boolean) {
        moveCamera(LatLng(address.latitude, address.longitude), zoomLevel, isAnimated)
    }

    private fun handleLocationState(locationState: DeviceLocationState) {
        when (locationState) {
            is DeviceLocationState.Success -> showLocation(locationState.location)
            is DeviceLocationState.Message -> processLocationMessages(locationState.message)
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

    private fun processLocationMessages(message: AppMessage) {
        when (message.ID) {
            PERMISSION_ERROR_ID -> checkPermissions()
            GOOGLE_PLAY_SERVICES_NOT_PRESENT_ID -> processNoPlayServicesError()
            LOCATION_IS_NOT_AVAILABLE_ID -> processLocationIsNotAvailableError()
            LOCATION_IS_AVAILABLE_ID -> processLocationIsAvailable(message.text)
            LOCATION_ERROR_ID -> makeSnackbar(binding.root, message.text)
        }
    }

    private fun processLocationIsNotAvailableError() {
        locationIsNotAvailableErrorFlag = true
        showLocationIsNotAvailableDialog()
    }

    private fun processNoPlayServicesError() {
        googlePlayServicesNotPresentErrorFlag = true
        showNoPlayServicesDialog()
    }

    private fun processLocationIsAvailable(text: String) {
        if (locationIsNotAvailableErrorFlag) {
            makeSnackbar(binding.root, text)
        }
        locationIsNotAvailableErrorFlag = false
    }

    private fun showLocationLoadingStatus() {}

    private fun handleAddressState(addressState: SearchAddressState) {
        when (addressState) {
            is SearchAddressState.Success -> showFoundAddresses(addressState.searchAddress)
            is SearchAddressState.Message -> makeSnackbar(binding.root, addressState.message.text)
            is SearchAddressState.Loading -> showAddressStateLoadingStatus()
        }
    }

    private fun showFoundAddresses(searchAddress: SearchAddress) {
        showAddressMarker(searchAddress.markerOptions)
        showAddress(searchAddress.address, ADDRESS_SEARCH_ZOOM_LEVEL, true)
    }

    private fun showAddressStateLoadingStatus() {}

    private fun handleChargeStationsState(state: ChargeStationsState) {
        when (state) {
            is ChargeStationsState.Success -> showChargeStationList(state.chargeStations)
            is ChargeStationsState.Message -> makeSnackbar(binding.root, state.message.text)
            is ChargeStationsState.Loading -> showChargeStationsLoadingStatus()
        }
    }

    private fun showChargeStationList(chargeStations: List<ChargeStation>) {
        chargeStations.forEach { chargeStation ->
            showChargeStationMarker(chargeStation)
        }
        clusterManager.cluster()
    }

    private fun showChargeStationsLoadingStatus() {}

    private fun applyFilters(filters: List<ChargeFilter>) {
        clusterManager.clearItems()
        mapsFragmentViewModel.requestChargeStations(filters)
    }

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
        AlertDialog.Builder(requireContext(), chargeit.core.R.style.AlertDialogStyle)
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
        private const val ZOOM_INCREMENT = 1f

        @JvmStatic
        fun newInstance() = MapsFragment()
    }

}