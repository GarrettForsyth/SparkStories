package com.example.android.writeitsayithearit.ui.cues.models

/**
 * This object represents the user input for a Story's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class CueTextField(var text: String = "") {
    fun isValid() = text.trim().length in minCharacters..maxCharacters

    companion object {
        const val minCharacters = 5
        const val maxCharacters = 200
    }
}