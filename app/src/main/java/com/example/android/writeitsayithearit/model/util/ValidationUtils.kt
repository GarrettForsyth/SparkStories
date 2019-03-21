package com.example.android.writeitsayithearit.model.util

/**
 * Replaces consecutive blank characters with a single instance of the matching blank character.
 *
 * Two new lines are allowed, but anymore will be compressed to one.
 *
 * @input the string which will have its multiple blank characters replaced.
 */
fun removeMultipleBlankCharacters(input: String): String {
    return  input.replace(Regex("  +|\t\t+|\r\r+|\n\n\n+"), { result ->
        result.groupValues.first().first().toString()
    })
}