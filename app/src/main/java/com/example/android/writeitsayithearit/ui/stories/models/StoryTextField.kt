package com.example.android.writeitsayithearit.ui.stories.models

/**
 * This object represents the user input for a Story's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class StoryTextField(var text: String = "") {
    fun isValid() = text.trim().length in minCharacters..maxCharacters

    companion object {
        const val minCharacters = 10
        const val maxCharacters = 5000
    }
}