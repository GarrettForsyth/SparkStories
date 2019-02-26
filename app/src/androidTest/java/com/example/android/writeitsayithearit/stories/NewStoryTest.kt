package com.example.android.writeitsayithearit.stories

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.R.id.*
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.model.story.StoryTextField
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to create new stories from a cue
 * So I can improve my writing skills and share my story with the community
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewStoryTest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private val cue = TestUtils.STARTING_CUES.first()
    private lateinit var scenario : ActivityScenario<MainActivity>

    @Before
    fun navigateToStoriesFragment(){
        // Given I have launched the app
        scenario = ActivityScenario.launch(MainActivity::class.java)

        // And clicked on a cue that interests me
        onView(withText(cue.text)).perform(click())
    }

    @Test
    fun toggleMenu() {
        // I should see the top menu
        onView(withId(new_story_top_menu)).check(matches(isDisplayed()))

        // When I click the toggle menu button
        onView(withId(R.id.toggle_menu_button)).perform(click())

        // I should no longer see the top menu
        onView(withId(new_story_top_menu)).check(matches(not(isDisplayed())))
    }

    @Test
    fun infoDialog() {
        // When I click the info button
        onView(withId(R.id.new_story_info_button)).perform(click())

        // I should see a dialog with explanation text
        onView(withText(R.string.new_story_info_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun togglePreviewMode() {
        // When I click the new story editText
        onView(withId(R.id.new_story_edit_text)).perform(click())

        // Then it should have focus
        onView(withId(R.id.new_story_edit_text)).check(matches(hasFocus()))

        // When I click preview button
        onView(withId(R.id.toggle_preview_button)).perform(click())
        // And I click the new story editText
        onView(withId(R.id.new_story_edit_text)).perform(click())

        // Then it should not have focus
        onView(withId(R.id.new_story_edit_text)).check(matches(not(hasFocus())))
        // And the keyboard should not be shown
        // TODO: learn how to check if keyboard is shown

        // When I click preview button again
        onView(withId(R.id.toggle_preview_button)).perform(click())
        // And I click the new story editText
        onView(withId(R.id.new_story_edit_text)).perform(click())

        // Then it should  have focus
        onView(withId(R.id.new_story_edit_text)).check(matches(hasFocus()))
    }

    @Test
    fun characterCount() {
        // When the fragment starts
        // Then the character count is coloured invalid
        // And is set to 0
        checkCharacterCount("", R.color.character_count_invalid )

        // When the text is just before the minimum valid character count
        val invalidMin = "a".repeat(StoryTextField.minCharacters -1)
        checkCharacterCount(invalidMin, R.color.character_count_invalid)

        // When the text is just at the minimum valid character count
        val validMin = "a".repeat(StoryTextField.minCharacters)
        checkCharacterCount(validMin, R.color.character_count_valid)

        // When the text is 51 characters before the max characters length
        val validEnd = "a".repeat(StoryTextField.maxCharacters -51)
        checkCharacterCount(validEnd, R.color.character_count_valid)

        // When the text is 50 characters before the max characters length
        val warningBegin = "a".repeat(StoryTextField.maxCharacters -50)
        checkCharacterCount(warningBegin, R.color.character_count_warning)

        // When the text is at max characters
        val warningEnd = "a".repeat(StoryTextField.maxCharacters)
        checkCharacterCount(warningEnd, R.color.character_count_warning)

        // When the text is 1 more than max characters
        val maxInvalid = "a".repeat(StoryTextField.maxCharacters + 1)
        checkCharacterCount(maxInvalid, R.color.character_count_invalid)
    }

    private fun checkCharacterCount(text: String, expectedColour: Int) {
        // When the text is 1 characters before the max characters length
        onView(withId(R.id.new_story_edit_text))
            .perform(replaceText(text))

        // Then the character count is coloured warning
        // And displays min count displays max - 1
        onView(withId(R.id.character_count_text_view))
            .check(matches(hasTextColor(expectedColour)))
            .check(matches(withText(text.length.toString())))
    }

    @Test
    fun createInvalidStory() {
        // When I leave the story text field blank
        // And I press the submit story button
        onView(withId(R.id.submit_story_button)).perform(click())

        // I should see the confirm story dialog
        onView(withText(R.string.confirm_submission_dialog_title))
            .check(matches(isDisplayed()))

        // When i press the confirm button
        onView(withId(android.R.id.button1)).perform(click())

        // I should see a snackbar explaining why the story is invalid
        onView(withText(R.string.invalid_new_story_snackbar))
    }

    @Test
    fun createAValidStory() {
        // When I type a valid story into the new story edit text field
        val validStoryText = "a".repeat(StoryTextField.minCharacters)
        onView(withId(R.id.new_story_edit_text))
            .perform(typeText(validStoryText))

        // And I press the submit story button
        onView(withId(R.id.submit_story_button)).perform(click())

        // I should see the confirm story dialog
        onView(withText(R.string.confirm_submission_dialog_title))
            .check(matches(isDisplayed()))

        // When I press the confirm button
        onView(withId(android.R.id.button1)).perform(click())

        // I should see a list of stories with my story in the list
        onView(withId(R.id.stories_list)).check(matches(isDisplayed()))
        onView(withText(validStoryText)).check(matches(isDisplayed()))
    }

}