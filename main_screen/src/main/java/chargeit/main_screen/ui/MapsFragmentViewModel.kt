package chargeit.main_screen.ui

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.core.viewmodel.CoreViewModel
import chargeit.main_screen.R
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.util.*

class MapsFragmentViewModel(
    private val application: Application
) : CoreViewModel() {
    private val _deviceLocationStateLD = MutableLiveData<DeviceLocationState>()
    val deviceLocationStateLD: LiveData<DeviceLocationState> by this::_deviceLocationStateLD

    private val _searchAddressStateLD = MutableLiveData<SearchAddressState>()
    val searchAddressStateLD: LiveData<SearchAddressState> by this::_searchAddressStateLD

    private val _chargeStationsStateLD = MutableLiveData<ChargeStationsState>()
    val chargeStationsStateLD: LiveData<ChargeStationsState> by this::_chargeStationsStateLD

    private val permissionError =
        DeviceLocationError(
            errorID = PERMISSION_ERROR_ID,
            message = application.getString(R.string.permission_error_message)
        )

    private val googlePlayServicesNotPresentError =
        DeviceLocationError(
            errorID = GOOGLE_PLAY_SERVICES_NOT_PRESENT_ERROR_ID,
            message = application.getString(R.string.google_play_services_not_present_error)
        )

    private val locationIsNotAvailableError =
        DeviceLocationError(
            errorID = LOCATION_IS_NOT_AVAILABLE_ERROR_ID,
            message = application.getString(R.string.location_is_not_available_error)
        )

    private val locationIsAvailableEvent =
        DeviceLocationEvent(
            eventID = LOCATION_IS_AVAILABLE_EVENT_ID,
            message = application.getString(R.string.location_is_available_event)
        )

    private val emptyQueryError =
        SearchAddressError(
            errorID = EMPTY_QUERY_ERROR_ID,
            message = application.getString(R.string.empty_query_error)
        )

    private val addressNotFoundError =
        SearchAddressError(
            errorID = ADDRESS_NOT_FOUND_ERROR_ID,
            message = application.getString(R.string.address_not_found_error)
        )

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private var locationUpdatesStartedFlag = false

    private val locationRequest = LocationRequest.Builder(DEVICE_LOCATION_REFRESH_PERIOD)
        .setDurationMillis(DEVICE_LOCATION_REQUEST_DURATION)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.forEach { location ->
                postDeviceLocationStateSuccess(createDeviceLocationObject(location))
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
            if (!p0.isLocationAvailable) {
                postDeviceLocationStateError(locationIsNotAvailableError)
            } else {
                postDeviceLocationStateEvent(locationIsAvailableEvent)
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        error.printStackTrace()
    }
    private val scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)


    private fun postDeviceLocationStateSuccess(deviceLocation: DeviceLocation) {
        _deviceLocationStateLD.postValue(DeviceLocationState.Success(deviceLocation))
    }

    private fun postDeviceLocationStateError(deviceLocationError: DeviceLocationError) {
        _deviceLocationStateLD.postValue(DeviceLocationState.Error(deviceLocationError))
    }

    private fun postDeviceLocationStateEvent(deviceLocationEvent: DeviceLocationEvent) {
        _deviceLocationStateLD.postValue(DeviceLocationState.Event(deviceLocationEvent))
    }

    private fun postDeviceLocationStateLoading() {
        _deviceLocationStateLD.postValue(DeviceLocationState.Loading)
    }

    private fun postSearchAddressStateSuccess(searchAddress: SearchAddress) {
        _searchAddressStateLD.postValue(SearchAddressState.Success(searchAddress))
    }

    private fun postSearchAddressStateError(searchAddressError: SearchAddressError) {
        _searchAddressStateLD.postValue(SearchAddressState.Error(searchAddressError))
    }

    private fun postSearchAddressStateLoading() {
        _searchAddressStateLD.postValue(SearchAddressState.Loading)
    }

    private fun postChargeStationsStateSuccess(chargeStations: List<ChargeStation>) {
        _chargeStationsStateLD.postValue(ChargeStationsState.Success(chargeStations))
    }

    private fun postChargeStationsStateError(error: Throwable) {
        _chargeStationsStateLD.postValue(ChargeStationsState.Error(error))
    }

    private fun postChargeStationsStateLoading() {
        _chargeStationsStateLD.postValue(ChargeStationsState.Loading)
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(application)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun getLocationMarkerOptions(title: String, location: Location) =
        MarkerOptions()
            .title(title)
            .position(LatLng(location.latitude, location.longitude))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_device_location_marker))

    private fun getAddressMarkerOptions(title: String, location: LatLng) =
        MarkerOptions().title(title).position(location)

    private fun getChargeStationMarkerOptions(title: String, location: LatLng) =
        getAddressMarkerOptions(title, location)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_charge_station_marker))

    private fun createDeviceLocationObject(location: Location): DeviceLocation {
        val address = getAddressByLocation(location)
        return DeviceLocation(
            address = address,
            markerOptions = getLocationMarkerOptions(
                application.getString(
                    R.string.device_location_marker_title,
                    address.getAddressLine(Int.ZERO)
                ), location
            )
        )
    }

    private fun createSearchAddressObject(address: Address): SearchAddress {
        return SearchAddress(
            address = address,
            markerOptions = getAddressMarkerOptions(
                address.getAddressLine(Int.ZERO),
                LatLng(address.latitude, address.longitude)
            )
        )
    }

    private fun createChargeStationObject(stationID: Int, location: LatLng): ChargeStation {
        return ChargeStation(
            stationID = stationID,
            markerOptions = getChargeStationMarkerOptions(
                application.getString(R.string.charge_station_title, stationID),
                location
            )
        )
    }

    private fun getAddressByLocation(location: Location): Address {
        val addresses = runBlocking(scope.coroutineContext) {
            Geocoder(application).getFromLocation(
                location.latitude,
                location.longitude,
                DEVICE_LOCATION_SEARCH_RESULTS
            ) ?: listOf(Address(Locale(String.EMPTY)))
        }
        return addresses.first()
    }

    private fun getAddressesByQuery(query: String) =
        Geocoder(application)
            .getFromLocationName(
                query,
                MAX_ADDRESS_SEARCH_RESULTS
            ) ?: listOf()

    private fun searchAddressesByQuery(query: String) {
        val addresses = getAddressesByQuery(query)
        if (addresses.isNotEmpty()) {
            val searchAddress = createSearchAddressObject(addresses.first())
            postSearchAddressStateSuccess(searchAddress)
        } else {
            postSearchAddressStateError(addressNotFoundError)
        }
    }

    private fun enableLocationUpdates() {
        postDeviceLocationStateLoading()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
        locationUpdatesStartedFlag = true
    }

    private fun checkPermissions() {
        if (isAtLeastOneGranted(application)) {
            enableLocationUpdates()
        } else {
            postDeviceLocationStateError(permissionError)
        }
    }

    fun checkQuery(query: String?) {
        scope.launch {
            postSearchAddressStateLoading()
            val resultQuery = query?.lowercase()?.trim() ?: String.EMPTY
            if (resultQuery.isNotBlank()) {
                searchAddressesByQuery(resultQuery)
            } else {
                postSearchAddressStateError(emptyQueryError)
            }
        }
    }

    fun startLocationUpdates() {
        if (!locationUpdatesStartedFlag) {
            if (isGooglePlayServicesAvailable()) {
                checkPermissions()
            } else {
                postDeviceLocationStateError(googlePlayServicesNotPresentError)
            }
        }
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        locationUpdatesStartedFlag = false
    }

    fun requestChargeStations() {
        postChargeStationsStateLoading()
        postChargeStationsStateSuccess(
            listOf(
                createChargeStationObject(STATION_ID, LatLng(STATION_LATITUDE, STATION_LONGITUDE))
            )
        )
    }

    companion object {
        private const val MAX_ADDRESS_SEARCH_RESULTS = 5
        private const val DEVICE_LOCATION_REFRESH_PERIOD = 2000L
        private const val DEVICE_LOCATION_REQUEST_DURATION = 60000L
        private const val DEVICE_LOCATION_SEARCH_RESULTS = 1
        private const val STATION_ID = 1
        private const val STATION_LATITUDE = 57.756310
        private const val STATION_LONGITUDE = 40.969853
    }
}