package chargeit.profilescreen

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.setEmptyError() {
    this.error = EMPTY_FIELD_ERROR
}

const val EMPTY_FIELD_ERROR = "Необходимо заполнить поле"