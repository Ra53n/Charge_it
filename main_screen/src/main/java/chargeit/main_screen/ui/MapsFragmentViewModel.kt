package chargeit.main_screen.ui

import android.app.Application
import android.content.Context
import android.location.*
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import chargeit.core.utils.EMPTY
import chargeit.core.viewmodel.CoreViewModel
import chargeit.main_screen.R
import chargeit.main_screen.domain.*
import chargeit.main_screen.settings.*
import chargeit.main_screen.utils.isPermissionGranted
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*

class MapsFragmentViewModel(
    private val application: Application,
    val locationLiveData: MutableLiveData<LocationState> = MutableLiveData(),
    val datasetLiveData: MutableLiveData<DatasetState> = MutableLiveData()
) : CoreViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private val scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)

    private val permissionError =
        LocationError(
            errorID = PERMISSION_ERROR_ID,
            message = application.getString(R.string.permission_error_message)
        )

    private val noProviderLocationPresentError =
        LocationError(
            errorID = NO_PROVIDER_LOCATION_PRESENT_ERROR_ID,
            message = application.getString(R.string.no_provider_location_present_error)
        )

    private val noProviderLocationNotPresentError =
        LocationError(
            errorID = NO_PROVIDER_LOCATION_NOT_PRESENT_ERROR_ID,
            message = application.getString(R.string.no_provider_location_not_present_error)
        )

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            postLocationSuccess(getDeviceLocation(location))
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {
            if (provider == PROVIDER) {
                whenProviderIsDisabled()
            }
        }
    }

    private val locationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private fun getLocation() {
        when (locationManager.isProviderEnabled(PROVIDER)) {
            true -> whenProviderIsEnabled()
            false -> whenProviderIsDisabled()
        }
    }

    private fun whenProviderIsDisabled() {
        when (val location = locationManager.getLastKnownLocation(PROVIDER)) {
            null -> whenLastLocationIsNotPresent()
            else -> whenLastLocationIsPresent(location)
        }
    }

    private fun whenLastLocationIsPresent(location: Location) {
        postLocationSuccess(getDeviceLocation(location))
        postLocationError(noProviderLocationPresentError)
    }

    private fun whenLastLocationIsNotPresent() {
        postLocationError(noProviderLocationNotPresentError)
    }

    private fun whenProviderIsEnabled() {
        if (locationManager.getProvider(PROVIDER) != null) {
            enableLocationUpdates(locationListener)
        }
    }

    private fun enableLocationUpdates(locationListener: LocationListener) {
        locationManager.requestLocationUpdates(
            PROVIDER,
            DEVICE_LOCATION_REFRESH_PERIOD,
            DEVICE_LOCATION_MINIMAL_DISTANCE,
            locationListener
        )
    }

    private fun postLocationSuccess(deviceLocation: DeviceLocation) {
        locationLiveData.postValue(LocationState.Success(deviceLocation))
    }

    private fun postLocationError(locationError: LocationError) {
        locationLiveData.postValue(LocationState.Error(locationError))
    }

    private fun postLocationLoading() {
        locationLiveData.postValue(LocationState.Loading)
    }

    private fun postDatasetSuccess(mapsFragmentDataset: MapsFragmentDataset) {
        datasetLiveData.postValue(DatasetState.Success(mapsFragmentDataset))
    }

    private fun postDatasetError(error: Throwable) {
        datasetLiveData.postValue(DatasetState.Error(error))
    }

    private fun postDatasetLoading() {
        datasetLiveData.postValue(DatasetState.Loading)
    }

    private fun getAddressByLocation(location: Location): Address {
        val addresses = runBlocking(scope.coroutineContext) {
            Geocoder(application.applicationContext).getFromLocation(
                location.latitude,
                location.longitude,
                DEVICE_LOCATION_SEARCH_RESULTS
            ) ?: listOf(Address(Locale(String.EMPTY)))
        }
        return addresses.first()
    }

    fun getAddressesByQuery(query: String): List<Address> {
        val addresses = runBlocking(scope.coroutineContext) {
            Geocoder(application.applicationContext).getFromLocationName(
                query,
                MAX_ADDRESS_SEARCH_RESULTS
            ) ?: listOf()
        }
        return addresses
    }

    fun getDeviceLocation(location: Location) = DeviceLocation(
        location = LatLng(location.latitude, location.longitude),
        address = getAddressByLocation(location)
    )

    fun startPostingLocationLiveData() {
        postLocationLoading()
        when (isPermissionGranted(application.applicationContext, PERMISSION)) {
            true -> getLocation()
            false -> postLocationError(permissionError)
        }
    }

    fun startPostingDatasetLiveData() {
        postDatasetSuccess(MapsFragmentDataset())
    }

}