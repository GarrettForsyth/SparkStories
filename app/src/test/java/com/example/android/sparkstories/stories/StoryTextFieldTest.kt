package com.example.android.sparkstories.stories

import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.story.StoryTextField
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class StoryTextFieldTest {

    private val storyTextField= StoryTextField()

    @Test
    fun validText() {
        storyTextField.text = NEW_STORY_TEXT_VALID
        assertTrue(storyTextField.isValid())
    }

    @Test
    fun invalidWhenTextTooShort() {
        storyTextField.text = NEW_STORY_TEXT_TOO_SHORT
        assertFalse(storyTextField.isValid())
    }

    @Test
    fun invalidWhenTextIsBlank() {
        storyTextField.text = NEW_STORY_TEXT_BLANK
        assertFalse(storyTextField.isValid())
    }

    @Test
    fun invalidWhenTextIsTooLong() {
        storyTextField.text = NEW_STORY_TEXT_TOO_LONG
        assertFalse(storyTextField.isValid())
    }

    companion object {
        private const val MINIMUM_STORY_LENGTH = StoryTextField.minCharacters
        private const val MAXIMUM_STORY_LENGTH = StoryTextField.maxCharacters

        private val NEW_STORY_TEXT_VALID = "a".repeat(MINIMUM_STORY_LENGTH)
        private val NEW_STORY_TEXT_TOO_SHORT = "a".repeat(MINIMUM_STORY_LENGTH - 1)
        private val NEW_STORY_TEXT_BLANK = " ".repeat(MINIMUM_STORY_LENGTH)
        private val NEW_STORY_TEXT_TOO_LONG = "a".repeat(MAXIMUM_STORY_LENGTH + 1)
    }
}