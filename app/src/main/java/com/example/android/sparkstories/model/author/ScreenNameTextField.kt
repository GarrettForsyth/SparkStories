package com.example.android.sparkstories.model.author

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.android.sparkstories.model.util.removeMultipleBlankCharacters

/**
 * This object represents the user input for a screen name text field.
 *
 * It is responsible for maintaining its validation state.
 */
class ScreenNameTextField(
    var _text: String = ""
): BaseObservable() {

    private val validScreenNameMatcher = Regex("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*\$")

    var text: String
        @Bindable get() {
            return _text
        }
        set(value) {
            if (_text != value) {
                _text = removeMultipleBlankCharacters(value)
                notifyPropertyChanged(BR.viewmodel)
            }
        }

    fun isValid() = validLength() && validSymbols()

    private fun validLength() = text.length in minCharacters..maxCharacters
    private fun validSymbols() = text.matches(validScreenNameMatcher)

    fun getErrorMessages(): String {
        val messages = StringBuilder()
        if (!validLength()) {
            messages.append("Screen name must be between $minCharacters and $maxCharacters.\n")
        }
        if (!validSymbols()) {
            messages.append("Screen name uses inappropriate characters.\n")
        }
        return messages.toString()
    }

    companion object {
        const val minCharacters = 3
        const val maxCharacters = 30
    }
}
