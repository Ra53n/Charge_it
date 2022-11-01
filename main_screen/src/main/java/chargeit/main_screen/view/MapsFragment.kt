package chargeit.main_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import chargeit.core.view.CoreFragment
import chargeit.main_screen.R
import chargeit.main_screen.databinding.FragmentMapsBinding
import chargeit.main_screen.domain.Place
import chargeit.main_screen.utils.hideKeyboard
import chargeit.main_screen.utils.makeSnackbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.String.format

class MapsFragment : CoreFragment(R.layout.fragment_maps) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val mainScreenViewModel: MapsFragmentViewModel by viewModel()

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
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_maps_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearch()
        initButtons()
    }

    private fun initSearch() {
        binding.fragmentMapsSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    view?.let {
                        it.hideKeyboard()
                        it.makeSnackbar(text = START_SEARCH_MESSAGE)
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
            fragmentMapsFilterScreenButton.setOnClickListener { filterScreenButtonClick() }
            fragmentMapsMyCoordsButton.setOnClickListener { myCoordsButtonClick() }
        }
    }

    private fun initMap() {
        val defaultPlace = mainScreenViewModel.getDefaultPlace()
        goToPlace(defaultPlace)
        map.addMarker(MarkerOptions().position(defaultPlace.coordinates).title(defaultPlace.name))
        map.setOnMarkerClickListener { marker ->
            view?.makeSnackbar(text = format(MARKER_CLICK_MESSAGE, marker.title))
            true
        }
    }

    private fun filterScreenButtonClick() {
        view?.makeSnackbar(text = FILTER_PAGE_MESSAGE)
    }

    private fun goToDefaultPlace() {
        goToPlace(mainScreenViewModel.getDefaultPlace())
    }

    private fun goToPlace(place: Place) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.coordinates, place.zoomLevel))
    }

    private fun myCoordsButtonClick() {
        goToDefaultPlace()
        view?.makeSnackbar(text = DEFAULT_PLACE_MESSAGE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_PLACE_MESSAGE = "Default place"
        private const val FILTER_PAGE_MESSAGE = "Filter page"
        private const val MARKER_CLICK_MESSAGE = "Marker %1\$s clicked!"
        private const val START_SEARCH_MESSAGE = "Searching!"

        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}