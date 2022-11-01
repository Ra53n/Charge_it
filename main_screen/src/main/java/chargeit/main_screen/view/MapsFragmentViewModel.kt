package chargeit.main_screen.view

import chargeit.core.viewmodel.CoreViewModel
import chargeit.main_screen.domain.Place
import chargeit.main_screen.settings.*
import com.google.android.gms.maps.model.LatLng

class MapsFragmentViewModel : CoreViewModel() {
    fun getDefaultPlace() = Place(
        name = DEFAULT_PLACE_NAME,
        tag = DEFAULT_PLACE_TAG,
        coordinates = LatLng(DEFAULT_PLACE_LATITUDE, DEFAULT_PLACE_LONGITUDE),
        zoomLevel = DEFAULT_PLACE_INIT_ZOOM_LEVEL
    )
}