package com.example.android.writeitsayithearit.model.story

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.writeitsayithearit.BR
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.model.util.removeMultipleBlankCharacters
import timber.log.Timber

/**
 * This object represents the user input for a Story's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class StoryTextField(
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

    val characterCountColour: Int
        get() {
            val length = text.trim().length
            return when {
                length < minCharacters -> R.color.character_count_invalid
                length < warningCharacters -> R.color.character_count_valid
                length <= maxCharacters -> R.color.character_count_warning
                else -> R.color.character_count_invalid
            }
        }

    companion object {
        const val minCharacters = 10
        const val maxCharacters = 5000
        const val warningCharacters = 4950
    }
}