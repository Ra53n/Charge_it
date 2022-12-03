package chargeit.main_screen.domain.search_addresses

import chargeit.main_screen.domain.message.AppMessage

sealed class SearchAddressState {
    data class Success(val searchAddress: SearchAddress) : SearchAddressState()
    data class Message(val message: AppMessage) : SearchAddressState()
    object Loading : SearchAddressState()
}