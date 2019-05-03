package com.example.android.sparkstories.authors

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.MainActivity
import com.example.android.sparkstories.R
import com.example.android.sparkstories.di.sparkservicestoriesmodules.NewUserSparkStoryServiceModule.Companion.TEST_USER_SCREEN_NAME
import com.example.android.sparkstories.di.SparkStoriesTestConfigurations
import com.example.android.sparkstories.model.author.ScreenNameTextField
import com.example.android.sparkstories.util.CountingAppExecutorsRule
import com.example.android.sparkstories.util.DataBindingIdlingResourceRule
import com.example.android.sparkstories.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a user
 * I want to create a screen name
 * So I can be credited with my cues/stories/narrations/comments
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewScreenNameTest {

    init {
        SparkStoriesTestConfigurations.injectNewUserTestAppComponent()
    }

    @Rule
    @JvmField
    val scenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutorsRule = CountingAppExecutorsRule()

    @Before
    fun initializeDatabindingIdlingResource() {
        DataBindingIdlingResourceRule(scenarioRule)
    }

    @Test
    fun initialViewState() {
        onView(withId(R.id.button_submit_screen_name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.edit_text_new_screen_name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.progress_bar_new_screen_name))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun successfulNewScreenName() {
        // The bottom nav is hidden
        onView(withId(R.id.bottom_navigation)).check(matches(not(isDisplayed())))


        onView(withId(R.id.edit_text_new_screen_name))
            .perform(typeText(
               TEST_USER_SCREEN_NAME
            ))

        onView(withId(R.id.button_submit_screen_name))
            .perform(click())

        onView(withId(R.id.cues_list)).check(matches(isDisplayed()))

        // The bottom nav is shown again
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))

    }

    @Test
    fun snackBarIsShownWhenUnsuccessful() {
        val newScreenName = "a".repeat(ScreenNameTextField.minCharacters - 1)
        onView(withId(R.id.edit_text_new_screen_name))
            .perform(replaceText(newScreenName))

        onView(withId(R.id.button_submit_screen_name))
            .perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()))

        onView(withId(R.id.progress_bar_new_screen_name))
            .check(matches(not(isDisplayed())))
    }

}
