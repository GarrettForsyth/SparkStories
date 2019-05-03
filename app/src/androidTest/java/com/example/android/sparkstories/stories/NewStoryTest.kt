package com.example.android.sparkstories.stories

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.MainActivity
import com.example.android.sparkstories.R
import com.example.android.sparkstories.R.id.*
import com.example.android.sparkstories.di.SparkStoriesTestConfigurations
import com.example.android.sparkstories.model.story.StoryTextField
import com.example.android.sparkstories.test.CustomMatchers.hasItemAtPosition
import com.example.android.sparkstories.test.CustomMatchers.withFontSize
import com.example.android.sparkstories.test.CustomViewActions.pinchIn
import com.example.android.sparkstories.test.CustomViewActions.pinchOut
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.util.CountingAppExecutorsRule
import com.example.android.sparkstories.util.DataBindingIdlingResourceRule
import com.example.android.sparkstories.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * As a writer
 * I want to create new stories from a cue
 * So I can improve my writing skills and share my story with the community
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewStoryTest {

    init { SparkStoriesTestConfigurations.injectAndroidTestAppComponent() }

    @Rule
    @JvmField
    val scenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutorsRule = CountingAppExecutorsRule()

    val dbSeed: DatabaseSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())

    private val cue = dbSeed.SEED_CUES.first()

    // Given I am logged in as the first seeded author (via dagger injection)
    private val currentUser = dbSeed.SEED_AUTHORS.first().name

    @Before
    fun navigateToStoriesFragment(){
        DataBindingIdlingResourceRule(scenarioRule)

        // And clicked on a cue that interests me
        onView(withText(startsWith(cue.text.take(30)))).perform(click())

        // And click on the new story button
        onView(withId(R.id.new_story_button)).perform(click())
    }

    @Test
    fun toggleMenu() {
        // I should see the top menu
        onView(withId(new_story_top_menu)).check(matches(isDisplayed()))

        // When I click the toggle menu button
        onView(withId(R.id.new_story_edit_text)).perform(doubleClick())

        // I should no longer see the top menu
        onView(withId(new_story_top_menu)).check(doesNotExist())
    }

    @Test
    fun viewCue() {
        onView(withId(R.id.view_cue_button)).perform(click())

        onView(withText(cue.text))
            .check(matches(isDisplayed()))

        onView(withText(cue.rating.toString()))
            .check(matches(isDisplayed()))

        onView(withText(cue.author))
            .check(matches(isDisplayed()))

        onView(withText(cue.formattedDate()))
            .check(matches(isDisplayed()))

        onView(withText(cue.formattedTime()))
            .check(matches(isDisplayed()))
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
        // also: espresso throws a performException trying to click the
        // toggle view button after adding state transitions to it

//        // When I click preview button again
//        onView(withId(R.id.toggle_preview_button)).perform(click())
//        // And I click the new story editText
//        onView(withId(R.id.new_story_edit_text)).perform(click())
//
//        // Then it should  have focus
//        onView(withId(R.id.new_story_edit_text)).check(matches(hasFocus()))
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

//    @Test
//    fun zoom() {
//
//        val defaultFontSize = 14f
//
//        // Font size starts at default size
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(defaultFontSize)))
//
//        // And pinch the screen out (twice to get max value)
//        onView((withId(R.id.new_story_edit_text))).perform(pinchOut())
//        onView((withId(R.id.new_story_edit_text))).perform(pinchOut())
//
//        // Font size now 3 times larger
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(3*defaultFontSize)))
//
//        // And pinch the screen in (twice to get min value)
//        onView((withId(R.id.new_story_edit_text))).perform(pinchIn())
//        onView((withId(R.id.new_story_edit_text))).perform(pinchIn())
//
//        // Font size is back to normal
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(defaultFontSize)))
//    }

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
        Timber.d(cue.text)

        Espresso.closeSoftKeyboard()

        // And I press the submit story button
        onView(withId(R.id.submit_story_button)).perform(click())

        // I should see the confirm story dialog
        onView(withText(R.string.confirm_submission_dialog_title))
            .check(matches(isDisplayed()))

        // When I press the confirm button
        onView(withId(android.R.id.button1)).perform(click())

        // I should see a list of stories with my story in the list
        onView(withText(validStoryText)).check(matches(isDisplayed()))
        onView(withId(R.id.stories_list))
            .check(
                matches(
                    allOf(
                        hasItemAtPosition(hasDescendant(withText(validStoryText)), 0),
                        hasItemAtPosition(hasDescendant(withText(0.toString())), 0),
                        hasItemAtPosition(hasDescendant(withText(currentUser)), 0)
                    )
                )
            )

        // When I navigate to cues
        onView(withId(R.id.cuesFragment)).perform(click())
        // Then I should see cue used in the story increased by one
        val expectedRating = cue.rating + 1
        onView(withId(R.id.cues_list))
            .check(matches(hasItemAtPosition(
                hasDescendant(withText(expectedRating.toString())), 0
            )))

    }
}