package chargeit.main_screen.view

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.Place
import chargeit.main_screen.settings.ADDRESS_SEARCH_ZOOM_LEVEL
import chargeit.main_screen.settings.EMPTY_STRING
import chargeit.main_screen.settings.MAX_ADDRESS_SEARCH_RESULTS
import chargeit.main_screen.settings.ZERO_INT
import chargeit.main_screen.utils.hideKeyboard
import chargeit.main_screen.utils.makeSnackbar
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

class MapsFragment : CoreFragment(R.layout.fragment_maps) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val mainScreenViewModel: MapsFragmentViewModel by viewModel()
    private var currentAddressMarker: Marker? = null
    private var currentUserMarker: Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        initMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(callback)
        initSearch()
        initButtons()
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
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

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
    }

    private fun initButtons() {
        with(binding) {
            filterScreenButton.setOnClickListener { filterScreenButtonClick() }
            userCoordinatesButton.setOnClickListener { userCoordinatesButtonClick() }
        }
    }

    private fun initMap() {
        goToDefaultPlace()
        map.setOnMarkerClickListener { marker ->
            view?.makeSnackbar(text = marker.title ?: getString(R.string.no_title_message))
            true
        }
    }

    private fun filterScreenButtonClick() {
        BottomSheetBehavior
            .from(binding.bottomSheetIncluded.bottomSheetRoot)
            .state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun goToDefaultPlace() {
        val defaultPlace = mainScreenViewModel.getDefaultPlace()
        currentUserMarker?.remove()
        currentUserMarker =
            addMarker(defaultPlace.coordinates, defaultPlace.name, R.drawable.ic_map_marker)
        goToPlace(defaultPlace)
    }

    private fun goToPlace(place: Place) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.coordinates, place.zoomLevel))
    }

    private fun userCoordinatesButtonClick() {
        goToDefaultPlace()
        view?.makeSnackbar(text = getString(R.string.default_place_message))
    }

    private fun searchByAddress(query: String, view: View) {
        Thread {
            try {
                val geoCoder = Geocoder(view.context)
                val addresses = geoCoder.getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS)
                if (addresses != null && addresses.size > ZERO_INT) {
                    val address = addresses[ZERO_INT]
                    view.post {
                        goToPlace(
                            Place(
                                name = address.getAddressLine(ZERO_INT),
                                tag = EMPTY_STRING,
                                coordinates = LatLng(address.latitude, address.longitude),
                                zoomLevel = ADDRESS_SEARCH_ZOOM_LEVEL
                            )
                        )
                        currentAddressMarker?.remove()
                        currentAddressMarker = map.addMarker(
                            MarkerOptions().position(LatLng(address.latitude, address.longitude))
                                .title(address.getAddressLine(ZERO_INT))
                        )
                    }
                } else {
                    view.makeSnackbar(text = getString(R.string.address_is_not_found_message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun addMarker(position: LatLng, title: String, icon: Int? = null): Marker? {
        val options = MarkerOptions().position(position).title(title)
        icon?.let {
            options.icon(BitmapDescriptorFactory.fromResource(icon))
        }
        return map.addMarker(options)
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