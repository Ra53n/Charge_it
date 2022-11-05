package chargeit.main_screen.view

import chargeit.core.viewmodel.CoreViewModel
import chargeit.main_screen.domain.Place

class MapsFragmentViewModel : CoreViewModel() {
    fun getDefaultPlace() = Place()
}