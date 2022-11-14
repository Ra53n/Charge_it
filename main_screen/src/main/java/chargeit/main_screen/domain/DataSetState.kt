package chargeit.main_screen.domain

sealed class DatasetState {
    data class Success(val mapsFragmentDataset: MapsFragmentDataset) : DatasetState()
    data class Error(val error: Throwable) : DatasetState()
    object Loading : DatasetState()
}