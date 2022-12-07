package chargeit.main_screen.ui.maps

import android.app.Application
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.main_screen.R
import chargeit.main_screen.data.MapsFragmentViewModelContract
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.domain.messages.AppMessage
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.domain.LocationMarker
import chargeit.main_screen.utils.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class MapsFragmentViewModel(
    private val application: Application,
    private val repo: LocalElectricStationRepo
) : CoreViewModel(), MapsFragmentViewModelContract {

    private val _messagesLiveData = MutableLiveData<AppMessage>()
    val messagesLiveData: LiveData<AppMessage> by this::_messagesLiveData

    private val _locationLiveData = MutableLiveData<LocationMarker>()
    val locationLiveData: LiveData<LocationMarker> by this::_locationLiveData

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private var locationUpdatesStartedFlag = false
    private var requestLocationShow = true
    private val defaultLocation = LatLng(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE)
    private var lastLocation = defaultLocation

    private val locationRequest = LocationRequest.Builder(DEVICE_LOCATION_REFRESH_PERIOD)
        .setDurationMillis(DEVICE_LOCATION_REQUEST_DURATION)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.forEach { location ->
                lastLocation = LatLng(location.latitude, location.longitude)
                if (requestLocationShow) {
                    _messagesLiveData.value =
                        AppMessage.MoveCamera(true, lastLocation, DEVICE_LOCATION_ZOOM_LEVEL)
                    requestLocationShow = false
                }
                val bitmapDescriptor = getBitmapFromAvailableSource(
                    application,
                    R.drawable.ic_location_marker,
                    R.drawable.ic_location_marker_backup
                )
                val options = MarkerOptions().icon(bitmapDescriptor).position(lastLocation)
                _locationLiveData.value = LocationMarker(options)
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { context, error ->
        when (context.job) {
            addressJob -> errorScope.launch {
                _messagesLiveData.value =
                    AppMessage.InfoSnackBar(application.getString(R.string.message_address_search_error))
            }
            locationAddressJob -> errorScope.launch {
                _messagesLiveData.value =
                    AppMessage.InfoSnackBar(application.getString(R.string.message_location_error))
            }
            requestJob -> errorScope.launch {
                _messagesLiveData.value =
                    AppMessage.InfoSnackBar(application.getString(R.string.message_stations_request_error))
            }
        }
        error.printStackTrace()
    }

    private var addressJob: Job? = null
    private var locationAddressJob: Job? = null
    private var requestJob: Job? = null

    private val mainScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    private val errorScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private fun searchAddressesByQuery(query: String) {
        val addresses = getAddressesByQuery(application, query, MAX_ADDRESS_SEARCH_RESULTS)
        if (addresses.isNotEmpty()) {
            val address = addresses.first()
            val position = LatLng(address.latitude, address.longitude)
            val bitmapDescriptor = getBitmapFromAvailableSource(
                application,
                R.drawable.ic_address_marker,
                R.drawable.ic_address_marker_backup
            )
            mainScope.launch(Dispatchers.Main) {
                _messagesLiveData.value =
                    AppMessage.MoveCamera(true, position, ADDRESS_SEARCH_ZOOM_LEVEL)
                _messagesLiveData.value =
                    AppMessage.AddressMarker(
                        MarkerOptions().icon(bitmapDescriptor).position(position)
                            .title(address.getAddressLine(Int.ZERO))
                    )
            }
        } else {
            mainScope.launch(Dispatchers.Main) {
                _messagesLiveData.value =
                    AppMessage.InfoSnackBar(application.getString(R.string.message_address_not_found))
            }
        }
    }

    private fun enableLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        locationUpdatesStartedFlag = true
    }

    private fun checkPermissions() {
        if (isAtLeastOnePermissionGranted(application)) {
            enableLocationUpdates()
        } else {
            _messagesLiveData.value =
                AppMessage.InfoSnackBar(application.getString(R.string.message_permission_error))
        }
    }

    override fun requestDefaultLocation() {
        _messagesLiveData.value =
            AppMessage.MoveCamera(false, defaultLocation, DEFAULT_LOCATION_ZOOM_LEVEL)
    }

    override fun requestAddressSearch(query: String?) {
        addressJob?.cancel()
        addressJob = mainScope.launch {
            val resultQuery = query?.lowercase()?.trim() ?: String.EMPTY
            if (resultQuery.isNotBlank()) {
                searchAddressesByQuery(resultQuery)
            } else {
                launch(Dispatchers.Main) {
                    _messagesLiveData.value =
                        AppMessage.InfoSnackBar(application.getString(R.string.message_empty_query))
                }
            }
        }
    }

    override fun startLocationUpdates() {
        if (locationUpdatesStartedFlag.not()) {
            if (isGooglePlayServicesAvailable(application)) {
                checkPermissions()
            } else {
                _messagesLiveData.value =
                    AppMessage.InfoDialog(
                        title = application.getString(R.string.no_play_services_title),
                        message = application.getString(R.string.no_play_services_message)
                    )
            }
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        locationUpdatesStartedFlag = false
    }

    override fun requestChargeStations(chargeFilters: FiltersMessage.ChargeFilters?) {
        requestJob?.cancel()
        requestJob = mainScope.launch {
            val globalStationModels = repo.getAllElectricStation()
            val resultModels = if (chargeFilters == null) {
                globalStationModels
            } else {
                globalStationModels.filter { model ->
                    getMatchSocketsCount(model, chargeFilters) > Int.ZERO
                }
            }
            val chargeStations = convertModelsToCLusterItems(application, resultModels)
            launch(Dispatchers.Main) {
                _messagesLiveData.value = AppMessage.ChargeStationMarkers(chargeStations)
            }
        }
    }

    override fun onFilterScreenButtonClick() {
        _messagesLiveData.value = AppMessage.Filters
    }

    override fun onDeviceLocationButtonClick() {
        _messagesLiveData.value = if (isGooglePlayServicesAvailable(application)) {
            AppMessage.MoveCamera(true, lastLocation, DEVICE_LOCATION_ZOOM_LEVEL)
        } else {
            AppMessage.InfoSnackBar(application.getString(R.string.message_google_play_services_not_present))
        }
    }

    override fun onClusterItemClick(clusterItem: MarkerClusterItem): Boolean {
        val distance = getDistanceBetween(lastLocation, clusterItem.position).first()
        val correctedDistance = (distance / HUNDRED).roundToInt().toDouble() / TEN
        _messagesLiveData.value = AppMessage.StationInfo(clusterItem.getEntity(), correctedDistance)
        return true
    }

    override fun onClusterClick(cluster: Cluster<MarkerClusterItem>?, currentZoom: Float): Boolean {
        if (cluster != null) {
            val position = cluster.position
            _messagesLiveData.value =
                AppMessage.MoveCamera(true, position, currentZoom + ZOOM_INCREMENT)
        }
        return true
    }

    override fun onLocationMarkerClick(location: LatLng): Boolean {
        locationAddressJob?.cancel()
        locationAddressJob = mainScope.launch {
            val address = getAddressByLocation(
                application,
                location,
                DEVICE_LOCATION_SEARCH_RESULTS,
                application.getString(R.string.message_unknown_address)
            )
            val text = application.getString(
                R.string.message_location_marker_title,
                address.getAddressLine(Int.ZERO)
            )
            launch(Dispatchers.Main) {
                _messagesLiveData.value = AppMessage.InfoSnackBar(text)
            }
        }
        return true
    }

    override fun onAddressMarkerClick(marker: Marker): Boolean {
        val title = marker.title ?: application.getString(R.string.message_unknown_marker)
        _messagesLiveData.value = AppMessage.InfoSnackBar(title)
        return true
    }

    override fun requestNotGrantedNoAskDialog() {
        _messagesLiveData.value = AppMessage.InfoDialog(
            title = application.getString(R.string.not_granted_no_ask_title),
            message = application.getString(R.string.not_granted_no_ask_message)
        )
    }

    override fun requestRationaleDialog() {
        _messagesLiveData.value = AppMessage.InfoDialog(
            title = application.getString(R.string.rationale_title),
            message = application.getString(R.string.rationale_message)
        )
    }

    companion object {
        private const val MAX_ADDRESS_SEARCH_RESULTS = 5
        private const val DEVICE_LOCATION_REFRESH_PERIOD = 2000L
        private const val DEVICE_LOCATION_REQUEST_DURATION = 60000L
        private const val DEVICE_LOCATION_SEARCH_RESULTS = 1
        private const val DEFAULT_LOCATION_LATITUDE = 55.751513
        private const val DEFAULT_LOCATION_LONGITUDE = 37.616655
        private const val DEFAULT_LOCATION_ZOOM_LEVEL = 10.0f
        private const val DEVICE_LOCATION_ZOOM_LEVEL = 15f
        private const val ADDRESS_SEARCH_ZOOM_LEVEL = 10.0f
        private const val ZOOM_INCREMENT = 1f
        private const val HUNDRED = 100
        private const val TEN = 10
    }
}
