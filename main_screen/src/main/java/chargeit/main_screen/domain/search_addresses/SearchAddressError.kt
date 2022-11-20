package chargeit.main_screen.domain.search_addresses

class SearchAddressError(val errorID: Int, message: String? = null) : Throwable(message)