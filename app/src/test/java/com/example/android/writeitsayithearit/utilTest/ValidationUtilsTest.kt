package com.example.android.writeitsayithearit.utilTest

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.model.util.removeMultipleBlankCharacters
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class ValidationUtilsTest {

    @Test
    fun removeMultipleSpaces() {
        val input = "The     dog  went to the    store."
        val expected = "The dog went to the store."
        assertEquals(expected, removeMultipleBlankCharacters(input))
    }

    @Test
    fun removeMultipleTabs() {
        val input = "The\t\t dog\t\t went\t\t\t\t to the store."
        val expected = "The\t dog\t went\t to the store."
        assertEquals(expected, removeMultipleBlankCharacters(input))
    }

    @Test
    fun removeMultipleEnters() {
        val input = "The\r\r dog\r\r\r\r went to the store."
        val expected = "The\r dog\r went to the store."
        assertEquals(expected, removeMultipleBlankCharacters(input))
    }

    @Test
    fun removeMultipleNewLines() {
        val input = "The\n\n dog\n\n\n\n went\n to the store."
        val expected = "The\n\n dog\n went\n to the store."
        assertEquals(expected, removeMultipleBlankCharacters(input))
    }

    @Test
    fun removeCombinationOfBlankCharacters() {
        val input = "The\r\r dog\t\t went  to the\n\n\n store."
        val expected = "The\r dog\t went to the\n store."
        assertEquals(expected, removeMultipleBlankCharacters(input))
    }
}
