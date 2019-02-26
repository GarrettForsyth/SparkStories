package com.example.android.writeitsayithearit.model.story

import com.example.android.writeitsayithearit.R

/**
 * This object represents the user input for a Story's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class StoryTextField(var text: String = "") {

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