package com.example.android.writeitsayithearit.stories

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.CustomMatchers.hasItemAtPosition
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_STRING_NO_MATCHES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_AUTHOR
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_AUTHOR_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_TEXT
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
import com.example.android.writeitsayithearit.ui.stories.StoryViewHolder
import com.example.android.writeitsayithearit.util.CountingAppExecutorsRule
import com.example.android.writeitsayithearit.util.DataBindingIdlingResourceRule
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to browse a list of stories
 * To learn from other people's writings
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class StoriesTest {

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

    @Before
    fun launchApp() {
        DataBindingIdlingResourceRule(scenarioRule)

        // Navigate to stories fragment via bottom nav
        onView(withId(R.id.storiesFragment)).perform(click())
    }

    @Test
    fun startingStoriesAreDisplayed() {
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun userFiltersByStoriesAuthor() {
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(STORY_FILTER_AUTHOR))

        verifyExpectedOrder(STORY_FILTER_AUTHOR_NEW_INDICES)
    }

    @Test
    fun noResults() {
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(FILTER_STRING_NO_MATCHES))

        onView(withId(R.id.no_results)).check(matches(isDisplayed()))

        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(""))
            .perform(replaceText(STORY_FILTER_TEXT))

        onView(withId(R.id.no_results)).check(matches(not(isDisplayed())))
    }

    @Test
    fun sortStoriesByNew() {
        selectSpinnerEntry("New")
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun sortStoriesByTop() {
        selectSpinnerEntry("Top")
        verifyExpectedOrder(SORT_TOP_INDICES)
    }

    @Test
    fun sortStoriesByHot() {
        selectSpinnerEntry("Hot")
        verifyExpectedOrder(SORT_HOT_INDICES)
    }

    @Test
    fun sortStoriesByHotAndFilter() {
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(STORY_FILTER_TEXT))

        selectSpinnerEntry("Hot")
        verifyExpectedOrder(STORY_FILTER_SORT_HOT_INDICES)
    }

    @Test
    fun sortStoriesByNewAndFilter() {
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(STORY_FILTER_TEXT))

        selectSpinnerEntry("New")
        verifyExpectedOrder(STORY_FILTER_SORT_NEW_INDICES)
    }

    @Test
    fun sortStoriesByTopAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(STORY_FILTER_TEXT))

        selectSpinnerEntry("Top")
        verifyExpectedOrder(STORY_FILTER_SORT_TOP_INDICES)
    }

    private fun selectSpinnerEntry(entry: String) {
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`(entry)
            )
        ).perform(click())
    }

    /**
     * Loops through a list of indices and checks that the starting
     * story associated with each index is in the correct order and
     * displayed in the list of stories.
     */
    private fun verifyExpectedOrder(expectedOrder: List<Int>) {
        Espresso.closeSoftKeyboard()
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedStory = dbSeed.SEED_STORIES[expectedIndex]
            onView(withId(R.id.stories_list))
                .perform(RecyclerViewActions.scrollToPosition<StoryViewHolder>(listPosition))
            onView(withId(R.id.stories_list))
                .check(
                    matches(
                        allOf(
                            hasItemAtPosition(hasDescendant(withText(startsWith(expectedStory.text.take(30)))), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.rating.toString())), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.author)), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.formattedTime())), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.formattedDate())), listPosition)
                        )
                    )
                )
        }
    }

}
