package com.example.android.writeitsayithearit.model.comment

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.writeitsayithearit.BR
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.model.util.removeMultipleBlankCharacters

/**
 * This object represents the user input for a Comment's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class CommentTextField(
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
        const val minCharacters = 0
        const val maxCharacters = 5000
    }
}
