package com.example.android.sparkstories.auth

import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.author.ScreenNameTextField
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class ScreenNameTextFieldTest {

    @Test
    fun validScreenName() {
        val screenName = ScreenNameTextField("a".repeat(ScreenNameTextField.minCharacters))
        assertTrue(screenName.isValid())
    }

    @Test
    fun tooShortScreenNameIsInvalid() {
        val screenName = ScreenNameTextField("a".repeat(ScreenNameTextField.minCharacters - 1))
        assertFalse(screenName.isValid())
    }

    @Test
    fun tooLongScreenNameIsInvalid() {
        val screenName = ScreenNameTextField("a".repeat(ScreenNameTextField.maxCharacters + 1))
        assertFalse(screenName.isValid())
    }

    @Test
    fun startsWithSpaceIsInvalid() {
        val screenName = ScreenNameTextField(" " + "a".repeat(ScreenNameTextField.minCharacters))
        assertFalse(screenName.isValid())
    }

    @Test
    fun startsWithSpaceIsUnderscoreIsInvalid() {
        val screenName = ScreenNameTextField("_" + "a".repeat(ScreenNameTextField.minCharacters))
        assertFalse(screenName.isValid())
    }

    @Test
    fun startsWithSpaceIsHyphenIsInvalid() {
        val screenName = ScreenNameTextField("-" + "a".repeat(ScreenNameTextField.minCharacters))
        assertFalse(screenName.isValid())
    }

    @Test
    fun endsWithSpaceIsInvalid() {
        val screenName = ScreenNameTextField("a".repeat(ScreenNameTextField.minCharacters) + " ")
        assertFalse(screenName.isValid())
    }

    @Test
    fun endsWithSpaceIsUnderscoreIsInvalid() {
        val screenName = ScreenNameTextField("a".repeat(ScreenNameTextField.minCharacters) + "_")
        assertFalse(screenName.isValid())
    }

    @Test
    fun endsWithSpaceIsHyphenIsInvalid() {
        val screenName = ScreenNameTextField( "a".repeat(ScreenNameTextField.minCharacters) + "-")
        assertFalse(screenName.isValid())
    }

    @Test
    fun multipleSpacesIsInavlid() {
        val screenName = ScreenNameTextField( "a".repeat(ScreenNameTextField.minCharacters) + "  " + "a")
        assertFalse(screenName.isValid())
    }

    @Test
    fun multipleUnderscoresIsInvalid() {
        val screenName = ScreenNameTextField( "a".repeat(ScreenNameTextField.minCharacters) + "__" + "a")
        assertFalse(screenName.isValid())
    }

    @Test
    fun multipleHypensIsInvalid() {
        val screenName = ScreenNameTextField( "a".repeat(ScreenNameTextField.minCharacters) + "--" + "a")
        assertFalse(screenName.isValid())
    }

    @Test
    fun punctuationIsInvalid() {
        val screenName = ScreenNameTextField( "a".repeat(ScreenNameTextField.minCharacters) + ".")
        assertFalse(screenName.isValid())
    }

}

