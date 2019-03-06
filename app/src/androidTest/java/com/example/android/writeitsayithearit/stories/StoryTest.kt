package com.example.android.writeitsayithearit.stories

import android.app.Application
import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.CustomMatchers.withFontSize
import com.example.android.writeitsayithearit.test.CustomViewActions.pinchIn
import com.example.android.writeitsayithearit.test.CustomViewActions.pinchOut
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
import com.example.android.writeitsayithearit.util.CountingAppExecutorsRule
import com.example.android.writeitsayithearit.util.DataBindingIdlingResourceRule
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import kotlinx.android.synthetic.main.fragment_new_story.*
import kotlinx.android.synthetic.main.fragment_story.*
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * As a writer
 * I want other writers to read stories
 * So I can learn from them, and share with them
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class StoryTest {

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

    private val story = dbSeed.SEED_STORIES.first()

    @Before
    fun navigateToStoriesFragment(){
        DataBindingIdlingResourceRule(scenarioRule)

        // And navigated to the stories list
        onView(withId(R.id.storiesFragment)).perform(click())

        // And clicked on the first story
        onView(withText(story.text)).perform(click())
    }

    @Test
    fun initialViewsVisible() {
        val cueText = dbSeed.SEED_CUES.first().text

        onView(withText(cueText))
            .check(matches(isDisplayed()))

        onView(withText(story.rating.toString()))
            .check(matches(isDisplayed()))

        onView(withText(story.text))
            .check(matches(isDisplayed()))

        onView(withText(story.rating.toString()))
            .check(matches(isDisplayed()))

        onView(withText(story.author))
            .check(matches(isDisplayed()))

        onView(withText(story.formattedDate()))
            .check(matches(isDisplayed()))
    }

    @Test
    fun toggleMenu() {
        // I should see the top menu
        onView(withId(R.id.story_top_menu)).check(matches(isDisplayed()))

        // When I click the toggle menu button
        onView(withId(R.id.toggle_menu_button)).perform(click())

        // I should no longer see the top menu
        onView(withId(R.id.story_top_menu)).check(matches(not(isDisplayed())))
    }

    @Test
    fun likeStory() {
        // Given the story's rating is displayed
        onView(withText(story.rating.toString()))
            .check(matches(isDisplayed()))

        // When I click the like story button
        onView(withId(R.id.like_story_button)).perform(click())

        // Then the given rating should increase by one
        val expectedRating = (story.rating + 1).toString()
        onView(withText(expectedRating))
            .check(matches(isDisplayed()))
    }

    @Test
    fun zoom() {
        val defaultFontSize = 14f

        // When I click the toggle menu button
        onView(withId(R.id.toggle_menu_button)).perform(click())

        // Font size starts at default size
        onView((withId(R.id.story_text_view)))
            .check(matches(withFontSize(defaultFontSize)))

        // And pinch the screen out (twice to get to max value)
        onView((withId(R.id.story_text_view))).perform(pinchOut())
        onView((withId(R.id.story_text_view))).perform(pinchOut())

        // Font size now 3 times larger
        onView((withId(R.id.story_text_view)))
            .check(matches(withFontSize(3*defaultFontSize)))

        // And pinch the screen in (twice to get min value)
        onView((withId(R.id.story_text_view))).perform(pinchIn())
        onView((withId(R.id.story_text_view))).perform(pinchIn())

        // Font size is back to normal
        onView((withId(R.id.story_text_view)))
            .check(matches(withFontSize(defaultFontSize)))
    }
}