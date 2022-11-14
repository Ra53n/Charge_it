package chargeit.main_screen.domain

class LocationError(val errorID: Int, message: String? = null) : Throwable(message)