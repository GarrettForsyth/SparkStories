package com.example.android.writeitsayithearit.model.comment

import com.example.android.writeitsayithearit.R

/**
 * This object represents the user input for a Comment's text field.
 *
 * It is responsible for maintaining its validation state.
 */
class CommentTextField(var text: String = "") {

    fun isValid() = text.trim().length in minCharacters..maxCharacters

    companion object {
        const val minCharacters = 0
        const val maxCharacters = 5000
    }
}
