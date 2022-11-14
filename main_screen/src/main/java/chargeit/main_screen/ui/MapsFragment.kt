package chargeit.main_screen.ui

import android.app.AlertDialog
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import chargeit.core.utils.ZERO
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.*
import chargeit.main_screen.settings.*
import chargeit.main_screen.utils.isPermissionGranted
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    private val defaultLocation = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE)
    private var currentDeviceLocation = defaultLocation

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            checkPermissionRequestResult(isGranted)
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
        initViewModel()
        initSearch()
        initButtons()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initMap()
        mapsFragmentViewModel.startPostingDatasetLiveData()
    }

    private fun initViewModel() {
        with(mapsFragmentViewModel) {
            datasetLiveData.observe(viewLifecycleOwner) { datasetState ->
                handleMainDataset(datasetState)
            }
            locationLiveData.observe(viewLifecycleOwner) { locationState ->
                handleLocation(locationState)
            }
        }
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    hideKeyboard(requireActivity())
                    val resultQuery = query.lowercase().trim()
                    when (resultQuery.isNotBlank()) {
                        true -> searchByAddress(resultQuery)
                        else -> whenEmptyQuery()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun whenEmptyQuery() {
        makeSnackbar(view = binding.root, text = getString(R.string.empty_query_message))
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

    private fun checkPermission() {
        if (!isPermissionGranted(requireContext(), PERMISSION)) {
            when (shouldShowRequestPermissionRationale(PERMISSION)) {
                true -> showRationaleDialog()
                false -> showNotGrantedNoAskDialog()
            }
        }
    }

    private fun startAccessToLocation() {
        when (isPermissionGranted(requireContext(), PERMISSION)) {
            true -> whenPermissionIsGranted()
            false -> permissionRequest.launch(PERMISSION)
        }
    }

    private fun checkPermissionRequestResult(isGranted: Boolean) {
        val noRationale = !shouldShowRequestPermissionRationale(PERMISSION)
        when {
            isGranted -> whenPermissionIsGranted()
            noRationale -> showNotGrantedNoAskDialog()
            else -> showRationaleDialog()
        }
    }

    private fun whenPermissionIsGranted() {
        mapsFragmentViewModel.startPostingLocationLiveData()
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
        checkPermission()
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
        when (isAnimated) {
            true -> map.animateCamera(cameraProperties)
            false -> map.moveCamera(cameraProperties)
        }
    }

    private fun changeDeviceLocationMarker(address: Address) {
        val title =
            getString(R.string.device_location_marker_title, address.getAddressLine(Int.ZERO))
        currentDeviceLocationMarker?.remove()
        currentDeviceLocationMarker = map.addMarker(
            MarkerOptions()
                .position(currentDeviceLocation)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_device_location_marker))
        )
    }

    private fun showAddress(address: Address, zoomLevel: Float, isAnimated: Boolean) {
        locationShowRequestFlag = false
        moveCamera(LatLng(address.latitude, address.longitude), zoomLevel, isAnimated)
    }

    private fun searchByAddress(query: String) {
        val addresses = mapsFragmentViewModel.getAddressesByQuery(query)
        when (addresses.isNotEmpty()) {
            true -> whenAddressIsNotEmpty(addresses.first())
            false -> whenAddressIsEmpty()
        }
    }

    private fun whenAddressIsNotEmpty(address: Address) {
        changeAddressMarker(address)
        showAddress(address, ADDRESS_SEARCH_ZOOM_LEVEL, true)
    }

    private fun changeAddressMarker(address: Address) {
        currentAddressMarker?.remove()
        currentAddressMarker = map.addMarker(
            MarkerOptions()
                .position(LatLng(address.latitude, address.longitude))
                .title(address.getAddressLine(Int.ZERO))
        )
    }

    private fun whenAddressIsEmpty() {
        makeSnackbar(
            view = binding.root,
            text = getString(R.string.address_is_not_found_message)
        )
    }

    private fun handleMainDataset(state: DatasetState) {
        when (state) {
            is DatasetState.Success -> whenDatasetIsSuccess(state.mapsFragmentDataset)
            is DatasetState.Error -> whenDatasetError(state.error)
            is DatasetState.Loading -> whenDatasetIsLoading()
        }
    }

    private fun whenDatasetIsSuccess(mapsFragmentDataset: MapsFragmentDataset) {
        addChargeStationMarker(mapsFragmentDataset.examplePlace)
    }

    private fun addChargeStationMarker(location: LatLng) {
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(getString(R.string.charge_station_marker_title))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_charge_station_marker))
        )
    }

    private fun whenDatasetError(error: Throwable) {}

    private fun whenDatasetIsLoading() {}

    private fun handleLocation(state: LocationState) {
        when (state) {
            is LocationState.Success -> whenLocationIsSuccess(state.deviceLocation)
            is LocationState.Error -> whenLocationError(state.locationError)
            is LocationState.Loading -> whenLocationIsLoading()
        }
    }

    private fun whenLocationIsSuccess(deviceLocation: DeviceLocation) {
        currentDeviceLocation =
            LatLng(deviceLocation.location.latitude, deviceLocation.location.longitude)
        changeDeviceLocationMarker(deviceLocation.address)
        if (locationShowRequestFlag) {
            showCurrentDeviceLocation()
        }
    }

    private fun whenLocationError(locationError: LocationError) {
        when (locationError.errorID) {
            PERMISSION_ERROR_ID -> whenPermissionError()
            NO_PROVIDER_LOCATION_PRESENT_ERROR_ID -> whenNoProviderLocationPresentError()
            NO_PROVIDER_LOCATION_NOT_PRESENT_ERROR_ID -> whenNoProviderLocationNotPresentError()
        }
    }

    private fun whenPermissionError() {
        checkPermission()
    }

    private fun whenNoProviderLocationPresentError() {
        showLastLocationIsPresentDialog()
    }

    private fun whenNoProviderLocationNotPresentError() {
        showLastLocationUnknownDialog()
    }

    private fun whenLocationIsLoading() {}

    private fun showRationaleDialog() {
        showInfoDialog(
            title = getString(R.string.rationale_title),
            message = getString(R.string.rationale_message),
            positiveButtonText = getString(R.string.rationale_positive_button)
        )
    }

    private fun showNotGrantedNoAskDialog() {
        showInfoDialog(
            title = getString(R.string.not_granted_no_ask_title),
            message = getString(R.string.not_granted_no_ask_message),
            positiveButtonText = getString(R.string.not_granted_no_ask_positive_button)
        )
    }

    private fun showLastLocationIsPresentDialog() {
        showInfoDialog(
            title = getString(R.string.last_location_title),
            message = getString(R.string.last_location_message),
            positiveButtonText = getString(R.string.last_location_positive_button)
        )
    }

    private fun showLastLocationUnknownDialog() {
        showInfoDialog(
            title = getString(R.string.no_last_location_title),
            message = getString(R.string.no_last_location_message),
            positiveButtonText = getString(R.string.no_last_location_positive_button)
        )
    }

    private fun showInfoDialog(title: String, message: String, positiveButtonText: String) {
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

        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}