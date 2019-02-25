package com.example.android.writeitsayithearit.stories

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.R.id.*
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.stories.models.StoryTextField
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
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