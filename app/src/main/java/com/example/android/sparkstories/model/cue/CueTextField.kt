package com.example.android.sparkstories.model.cue

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.sparkstories.BR
import com.example.android.sparkstories.model.util.removeMultipleBlankCharacters

/**
 * This object represents the user input for a Story's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class CueTextField(
    var _text: String = ""
): BaseObservable() {

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

    fun isValid() = text.trim().length in minCharacters..maxCharacters

    companion object {
        const val minCharacters = 5
        const val maxCharacters = 200
    }
}