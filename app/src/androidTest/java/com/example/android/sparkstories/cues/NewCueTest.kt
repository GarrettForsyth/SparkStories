package com.example.android.sparkstories.cues

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.android.sparkstories.MainActivity
import com.example.android.sparkstories.R
import com.example.android.sparkstories.di.SparkStoriesTestConfigurations
import com.example.android.sparkstories.model.cue.CueTextField
import com.example.android.sparkstories.test.CustomMatchers.hasItemAtPosition
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.util.CountingAppExecutorsRule
import com.example.android.sparkstories.util.DataBindingIdlingResourceRule
import com.example.android.sparkstories.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule


/**
 * As a writer
 * I want to create a new cue
 * To inspire other writers in the community to write
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewCueTest {

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

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())

    // And I am logged in as the first seeded author (via dagger injection)
    private val currentUser = dbSeed.SEED_AUTHORS.first().name

    @Before
    fun navigateToCuesFragment(){
        DataBindingIdlingResourceRule(scenarioRule)

        // And clicked on the add cue fab
        onView(withId(R.id.add_cue_fab)).perform(click())
    }

    @Test
    fun infoDialog() {
        // Then I should see a dialog with explanation text
        onView(withText(R.string.new_cue_info_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createInvalidCue() {
        // When I leave the cue text field blank
        // And I press the submit cue button
        onView(withId(R.id.submit_cue_btn)).perform(click())

        // I should see a snackbar explaining why the cue is invalid
        onView(withText(R.string.invalid_new_cue_snackbar))
    }

    @Test
    fun createAValidCue() {
        // When I type a valid cue into the new cue edit text field
        val validCueText = "a".repeat(CueTextField.minCharacters)
        onView(withId(R.id.new_cue_edit_text))
            .perform(typeText(validCueText))

        onView(withId(R.id.submit_cue_btn)).perform(click())

        // I should see a list of cues with my story in the list
        onView(withId(R.id.cues_list)).check(matches(isDisplayed()))
        onView(withText(validCueText)).check(matches(isDisplayed()))

        onView(withId(R.id.cues_list))
            .check(
                matches(
                    allOf(
                        hasItemAtPosition(hasDescendant(withText(validCueText)), 0),
                        hasItemAtPosition(hasDescendant(withText(0.toString())), 0),
                        hasItemAtPosition(hasDescendant(withText(currentUser)), 0)
                    )
                )
            )

    }
}

