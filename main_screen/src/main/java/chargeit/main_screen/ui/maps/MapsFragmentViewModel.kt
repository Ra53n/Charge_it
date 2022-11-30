package chargeit.main_screen.ui.maps

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chargeit.core.utils.EMPTY
import chargeit.core.utils.ZERO
import chargeit.core.viewmodel.CoreViewModel
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.repository.LocalElectricStationRepo
import chargeit.data.room.mappers.ElectricStationModelToEntityMapper
import chargeit.data.room.model.ElectricStationModel
import chargeit.main_screen.R
import chargeit.main_screen.data.MarkerClusterItem
import chargeit.main_screen.domain.charge_stations.ChargeStation
import chargeit.main_screen.domain.charge_stations.ChargeStationsState
import chargeit.main_screen.domain.device_location.DeviceLocation
import chargeit.main_screen.domain.device_location.DeviceLocationState
import chargeit.main_screen.domain.filters.ChargeFilter
import chargeit.main_screen.domain.message.AppMessage
import chargeit.main_screen.domain.search_addresses.SearchAddress
import chargeit.main_screen.domain.search_addresses.SearchAddressState
import chargeit.main_screen.settings.*
import chargeit.main_screen.utils.isAtLeastOnePermissionGranted
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.util.*

class MapsFragmentViewModel(
    private val application: Application,
    private val repo: LocalElectricStationRepo
) : CoreViewModel() {
    private val _deviceLocationStateLD = MutableLiveData<DeviceLocationState>()
    val deviceLocationStateLD: LiveData<DeviceLocationState> by this::_deviceLocationStateLD

    private val _searchAddressStateLD = MutableLiveData<SearchAddressState>()
    val searchAddressStateLD: LiveData<SearchAddressState> by this::_searchAddressStateLD

    private val _chargeStationsStateLD = MutableLiveData<ChargeStationsState>()
    val chargeStationsStateLD: LiveData<ChargeStationsState> by this::_chargeStationsStateLD

    private val permissionError =
        AppMessage(
            ID = PERMISSION_ERROR_ID,
            text = application.getString(R.string.message_permission_error)
        )

    private val googlePlayServicesNotPresentError =
        AppMessage(
            ID = GOOGLE_PLAY_SERVICES_NOT_PRESENT_ID,
            text = application.getString(R.string.message_google_play_services_not_present)
        )

    private val locationIsNotAvailableError =
        AppMessage(
            ID = LOCATION_IS_NOT_AVAILABLE_ID,
            text = application.getString(R.string.message_location_is_not_available)
        )

    private val locationError =
        AppMessage(
            ID = LOCATION_ERROR_ID,
            text = application.getString(R.string.message_location_error)
        )

    private val locationIsAvailableMessage =
        AppMessage(
            ID = LOCATION_IS_AVAILABLE_ID,
            text = application.getString(R.string.message_location_is_available)
        )

    private val emptyQueryError =
        AppMessage(
            ID = EMPTY_QUERY_ID,
            text = application.getString(R.string.message_empty_query)
        )

    private val addressNotFoundError =
        AppMessage(
            ID = ADDRESS_NOT_FOUND_ID,
            text = application.getString(R.string.message_address_not_found)
        )

    private val addressSearchError =
        AppMessage(
            ID = ADDRESS_SEARCH_ERROR_ID,
            text = application.getString(R.string.message_address_search_error)
        )

    private val stationsRequestError =
        AppMessage(
            ID = STATIONS_REQUEST_ERROR_ID,
            text = application.getString(R.string.message_stations_request_error)
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
                locationJob?.cancel()
                locationJob = scope.launch {
                    postDeviceLocationStateSuccess(createDeviceLocationObject(location))
                }
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            super.onLocationAvailability(availability)
            if (availability.isLocationAvailable.not()) {
                postDeviceLocationStateMessage(locationIsNotAvailableError)
            } else {
                postDeviceLocationStateMessage(locationIsAvailableMessage)
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { context, error ->
        when (context.job) {
            addressJob -> postSearchAddressStateMessage(addressSearchError)
            locationJob -> postDeviceLocationStateMessage(locationError)
            requestJob -> postChargeStationsStateMessage(stationsRequestError)
        }
        error.printStackTrace()
    }

    private var addressJob: Job? = null
    private var locationJob: Job? = null
    private var requestJob: Job? = null

    private val scope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    private fun postDeviceLocationStateSuccess(deviceLocation: DeviceLocation) {
        _deviceLocationStateLD.postValue(DeviceLocationState.Success(deviceLocation))
    }

    private fun postDeviceLocationStateMessage(message: AppMessage) {
        _deviceLocationStateLD.postValue(DeviceLocationState.Message(message))
    }

    private fun postDeviceLocationStateLoading() {
        _deviceLocationStateLD.postValue(DeviceLocationState.Loading)
    }

    private fun postSearchAddressStateSuccess(searchAddress: SearchAddress) {
        _searchAddressStateLD.postValue(SearchAddressState.Success(searchAddress))
    }

    private fun postSearchAddressStateMessage(message: AppMessage) {
        _searchAddressStateLD.postValue(SearchAddressState.Message(message))
    }

    private fun postSearchAddressStateLoading() {
        _searchAddressStateLD.postValue(SearchAddressState.Loading)
    }

    private fun postChargeStationsStateSuccess(chargeStations: List<ChargeStation>) {
        _chargeStationsStateLD.postValue(ChargeStationsState.Success(chargeStations))
    }

    private fun postChargeStationsStateMessage(message: AppMessage) {
        _chargeStationsStateLD.postValue(ChargeStationsState.Message(message))
    }

    private fun postChargeStationsStateLoading() {
        _chargeStationsStateLD.postValue(ChargeStationsState.Loading)
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(application)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun getBitmapDescriptorFromVector(
        vectorResId: Int
    ): BitmapDescriptor? {
        return ContextCompat.getDrawable(application, vectorResId)?.run {
            setBounds(Int.ZERO, Int.ZERO, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun getBestAvailableBitmap(vectorResID: Int, rasterResID: Int): BitmapDescriptor =
        getBitmapDescriptorFromVector(vectorResID)
            ?: BitmapDescriptorFactory.fromResource(rasterResID)

    private fun getLocationMarkerOptions(title: String, location: Location) =
        MarkerOptions()
            .title(title)
            .position(LatLng(location.latitude, location.longitude))
            .icon(
                getBestAvailableBitmap(
                    R.drawable.ic_location_marker,
                    R.drawable.ic_location_marker_backup
                )
            )

    private fun getAddressMarkerOptions(title: String, location: LatLng) =
        MarkerOptions().title(title).position(location).icon(
            getBestAvailableBitmap(
                R.drawable.ic_address_marker,
                R.drawable.ic_address_marker_backup
            )
        )

    private fun getChargeStationClusterItem(
        title: String,
        location: LatLng,
        snippet: String,
        entity: ElectricStationEntity
    ) = MarkerClusterItem(
        position = location,
        title = title,
        snippet = snippet,
        icon = getBestAvailableBitmap(
            R.drawable.ic_station_marker,
            R.drawable.ic_station_marker_backup
        ),
        entity = entity
    )

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

    private fun createChargeStationObject(entity: ElectricStationEntity): ChargeStation {
        return ChargeStation(
            info = entity,
            clusterItem = getChargeStationClusterItem(
                createMarkerTitle(entity),
                LatLng(entity.lat, entity.lon),
                String.EMPTY,
                entity
            )
        )
    }

    private fun createMarkerTitle(entity: ElectricStationEntity) =
        buildString {
            append(entity.titleStation)
            append(STRING_SEPARATOR)
            append(entity.description)
            append(STRING_SEPARATOR)
            append(entity.workTime)
        }

    private fun getAddressByLocation(location: Location): Address {
        val addresses =
            Geocoder(application).getFromLocation(
                location.latitude,
                location.longitude,
                DEVICE_LOCATION_SEARCH_RESULTS
            ) ?: listOf(Address(Locale(String.EMPTY)))
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
            postSearchAddressStateMessage(addressNotFoundError)
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
        if (isAtLeastOnePermissionGranted(application)) {
            enableLocationUpdates()
        } else {
            postDeviceLocationStateMessage(permissionError)
        }
    }

    private fun getMatchSocketsCount(model: ElectricStationModel, filters: List<ChargeFilter>) =
        model.listOfSockets.count { socket ->
            filters.count { filter -> filter.id == socket.id && filter.isChecked } > Int.ZERO
        }

    fun checkQuery(query: String?) {
        addressJob?.cancel()
        addressJob = scope.launch {
            postSearchAddressStateLoading()
            val resultQuery = query?.lowercase()?.trim() ?: String.EMPTY
            if (resultQuery.isNotBlank()) {
                searchAddressesByQuery(resultQuery)
            } else {
                postSearchAddressStateMessage(emptyQueryError)
            }
        }
    }

    fun startLocationUpdates() {
        if (locationUpdatesStartedFlag.not()) {
            if (isGooglePlayServicesAvailable()) {
                checkPermissions()
            } else {
                postDeviceLocationStateMessage(googlePlayServicesNotPresentError)
            }
        }
    }

    fun stopLocationUpdates() {
        if (locationUpdatesStartedFlag) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            locationUpdatesStartedFlag = false
        }
    }

    fun requestChargeStations(filters: List<ChargeFilter>? = null) {
        requestJob?.cancel()
        requestJob = scope.launch {
            val mapper = ElectricStationModelToEntityMapper()
            postChargeStationsStateLoading()
            val globalStationModels = repo.getAllElectricStation()
            val resultModels = if (filters.isNullOrEmpty()) {
                globalStationModels
            } else {
                globalStationModels.filter { model ->
                    getMatchSocketsCount(model, filters) > Int.ZERO
                }
            }
            val chargeStations =
                resultModels.map { model -> createChargeStationObject(mapper.map(model)) }
            postChargeStationsStateSuccess(chargeStations)
        }
    }

    companion object {
        private const val MAX_ADDRESS_SEARCH_RESULTS = 5
        private const val DEVICE_LOCATION_REFRESH_PERIOD = 2000L
        private const val DEVICE_LOCATION_REQUEST_DURATION = 60000L
        private const val DEVICE_LOCATION_SEARCH_RESULTS = 1
        private const val STRING_SEPARATOR = "\n"
    }
}