package chargeit.main_screen.domain.search_addresses

sealed class SearchAddressState {
    data class Success(val searchAddress: SearchAddress) : SearchAddressState()
    data class Error(val searchAddressError: SearchAddressError) : SearchAddressState()
    object Loading : SearchAddressState()
}