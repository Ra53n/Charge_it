package chargeit.data.repository

import chargeit.data.domain.model.Socket

interface LocalSocketRepo {

    fun getAllSocket(): List<Socket>
}