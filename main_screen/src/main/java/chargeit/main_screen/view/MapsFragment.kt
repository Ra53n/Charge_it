package chargeit.main_screen.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import chargeit.core.utils.ZERO
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.settings.*
import chargeit.main_screen.utils.hideKeyboard
import chargeit.main_screen.utils.makeSnackbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : CoreFragment(R.layout.fragment_maps) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val mainScreenViewModel: MapsFragmentViewModel by viewModel()
    private var currentAddressMarker: Marker? = null
    private var currentUserLocation: LatLng? = null
    private var userLocationJob: Job? = null
    private var addressLocationJob: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    private val scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)

    private val mapReadyCallback = OnMapReadyCallback { googleMap ->
        map = googleMap
        initMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(mapReadyCallback)
        initSearch()
        initButtons()
    }

    private fun checkPermission(context: FragmentActivity) {
        when {
            isPermissionGranted(context, AFL) -> getLocation(context)
            shouldShowRequestPermissionRationale(AFL) -> showRationaleDialog(context)
            else -> requestPermission(AFL, REQUEST_CODE)
        }
    }

    private fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun showRationaleDialog(context: Context) {
        AlertDialog.Builder(context).setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access))
            { _, _ -> requestPermission(AFL, REQUEST_CODE) }
            .setNegativeButton(getString(R.string.dialog_rationale_decline))
            { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        activity?.let { thisActivity ->
            checkPermissionsResult(thisActivity, requestCode, grantResults)
        }
    }

    private fun checkPermissionsResult(
        context: FragmentActivity,
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults.size ==
                grantResults.count { item -> item == PackageManager.PERMISSION_GRANTED }
            ) {
                getLocation(context)
            } else {
                showDialog(
                    context,
                    title = getString(R.string.dialog_title_no_gps),
                    message = getString(R.string.dialog_message_no_gps)
                )
            }
        }
    }

    private fun showDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setNegativeButton(getString(R.string.dialog_button_close))
            { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun getLocation(context: Context) {
        if (isPermissionGranted(context, AFL)) {
            with(map) {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = false
                uiSettings.isRotateGesturesEnabled = false
            }
            val onLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    currentUserLocation = LatLng(location.latitude, location.longitude)
                    showCurrentUserLocation(context, location)
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(PROVIDER)) {
                val provider = locationManager.getProvider(PROVIDER)
                provider?.let {
                    locationManager.requestLocationUpdates(
                        PROVIDER,
                        USER_LOCATION_REFRESH_PERIOD,
                        USER_LOCATION_MINIMAL_DISTANCE,
                        onLocationListener
                    )
                }
            } else {
                val location = locationManager.getLastKnownLocation(PROVIDER)
                location?.let {
                    currentUserLocation = LatLng(location.latitude, location.longitude)
                    showCurrentUserLocation(context, location)
                    showDialog(
                        context,
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_known_location)
                    )
                } ?: run {
                    showDialog(
                        context,
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_location_unknown)
                    )
                }
            }
        } else {
            showRationaleDialog(context)
        }
    }

    private fun showCurrentUserLocation(context: Context, location: Location) {
        userLocationJob?.cancel()
        userLocationJob = scope.launch {
            val addresses =
                Geocoder(context).getFromLocation(
                    location.latitude,
                    location.longitude,
                    USER_LOCATION_SEARCH_RESULTS
                )
            withContext(Dispatchers.Main) {
                showAddress(addresses.component1(), USER_LOCATION_ZOOM_LEVEL)
            }
        }
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                view?.let { thisView ->
                    query?.let { thisQuery ->
                        thisView.hideKeyboard()
                        val resQuery = thisQuery.lowercase().trim()
                        if (resQuery.isNotBlank()) {
                            searchByAddress(resQuery, thisView)
                        } else {
                            thisView.makeSnackbar(text = getString(R.string.empty_query_message))
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun initButtons() {
        with(binding) {
            filterScreenButton.setOnClickListener { filterScreenButtonClick() }
            userCoordinatesButton.setOnClickListener { userCoordinatesButtonClick() }
        }
    }

    private fun initMap() {
        userCoordinatesButtonClick()
        map.setOnMarkerClickListener { marker ->
            view?.makeSnackbar(text = marker.title ?: getString(R.string.no_title_message))
            true
        }
    }

    private fun filterScreenButtonClick() {
        BottomSheetBehavior.from(binding.bottomSheetIncluded.bottomSheetRoot).state =
            BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun userCoordinatesButtonClick() {
        currentUserLocation?.let { location ->
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(location, USER_LOCATION_ZOOM_LEVEL)
            )
        } ?: run {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE),
                    DEFAULT_PLACE_ZOOM_LEVEL
                )
            )
        }
        activity?.let { thisActivity -> checkPermission(thisActivity) }
    }

    private fun showAddress(address: Address, zoomLevel: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(address.latitude, address.longitude),
                zoomLevel
            )
        )
    }

    private fun searchByAddress(query: String, view: View) {
        addressLocationJob?.cancel()
        addressLocationJob = scope.launch {
            val addresses =
                Geocoder(view.context).getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS)
            if (addresses.isNotEmpty()) {
                val address = addresses.component1()
                withContext(Dispatchers.Main) {
                    showAddress(address, ADDRESS_SEARCH_ZOOM_LEVEL)
                    currentAddressMarker?.remove()
                    currentAddressMarker = map.addMarker(
                        MarkerOptions().position(LatLng(address.latitude, address.longitude))
                            .title(address.getAddressLine(Int.ZERO))
                    )
                }
            } else {
                view.makeSnackbar(text = getString(R.string.address_is_not_found_message))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    companion object {
        private const val AFL = Manifest.permission.ACCESS_FINE_LOCATION
        private const val PROVIDER = LocationManager.NETWORK_PROVIDER
        private const val REQUEST_CODE = 1

        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}