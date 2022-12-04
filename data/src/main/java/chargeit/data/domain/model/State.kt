package chargeit.data.domain.model

sealed class State {
    object Success : State()
    object Error : State()
}