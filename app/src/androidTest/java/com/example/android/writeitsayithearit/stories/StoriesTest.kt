package com.example.android.writeitsayithearit.stories

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.CustomMatchers.Companion.hasItemAtPosition
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_AUTHOR_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_STORIES
import com.example.android.writeitsayithearit.ui.stories.StoryViewHolder
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import kotlinx.android.synthetic.main.fragment_cues.*
import kotlinx.android.synthetic.main.fragment_stories.*
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
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun launchApp() {
        // Given I have launched the app
        // And the database is seeded with 10 starting stories,
        //  one of which contains the text 'Dogs'
        //  each with different ratings and creation dates
        scenario = ActivityScenario.launch(MainActivity::class.java)

        // Navigate to stories fragment via bottom nav
        onView(withId(R.id.storiesFragment)).perform(click())
    }

    @Test
    fun startingStoriesAreDisplayed() {
        // Then the list of starting story are displayed in the stories list
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun userFiltersByStoryText() {
        // When I type 'Dogs' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(FILTER_STRING))

        // Then the starting list should filter everything but the
        // one starting story containing the word dogs
        scenario.onActivity {
            assert(it.stories_list.adapter?.itemCount!! == 1)
        }

        val expectedText = STARTING_STORIES[6].text
        onView(withId(R.id.stories_list))
            .check(matches(hasDescendant(withText(expectedText))))
    }

    @Test
    fun userFiltersByStoriesAuthor() {
        // When I type 'Bob' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(FILTER_AUTHOR))

        // Then the starting list should filter everything but the
        // three  starting cue containing the author Bob
        scenario.onActivity {
            assert(it.stories_list.adapter?.itemCount!! == 3)
        }

        verifyExpectedOrder(FILTER_AUTHOR_NEW_INDICES)
    }

    @Test
    fun noResults() {
        // When I type 'zzz' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(FILTER_STRING_NO_MATCHES))

        // Then the no results text view is shown
        onView(withId(R.id.no_results)).check(matches(isDisplayed()))

        // When I type 'Dogs' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(replaceText(""))
            .perform(typeText(FILTER_STRING))

        // Then the no results text view is hidden
        onView(withId(R.id.no_results)).check(matches(not(isDisplayed())))
    }

    @Test
    fun sortStoriesByNew() {
        // When I chooses order by 'new' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("New")
            )
        ).perform(click())

        // Then the stories are sorted by their creation date
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun sortStoriesByTop() {
        // When I chooses order by 'top' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Top")
            )
        ).perform(click())

        // Then the stories are sorted by rating
        verifyExpectedOrder(SORT_TOP_INDICES)
    }

    @Test
    fun sortStoriesByHot() {
        // When I chooses order by 'hot' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Hot")
            )
        ).perform(click())

        // Then the list is ordered by rating and only includes
        // stories from the last day
        verifyExpectedOrder(SORT_HOT_INDICES)
    }

    @Test
    fun sortStoriesByHotAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(TestUtils.FILTER_STRING_TO))

        // And: I chooses order by 'hot' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Hot")
            )
        ).perform(click())

        // Then the list is sorted by 'hot' and filtered by 'to'
        verifyExpectedOrder(FILTER_SORT_HOT_INDICES)
    }

    @Test
    fun sortStoriesByNewAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(TestUtils.FILTER_STRING_TO))

        // And I chooses order by 'new' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("New")
            )
        ).perform(click())

        // Then the story are ordered by 'new' and filtered by 'to'
        verifyExpectedOrder(FILTER_SORT_NEW_INDICES)
    }

    @Test
    fun sortStoriesByTopAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(TestUtils.FILTER_STRING_TO))

        // And I chooses order by 'top' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Top")
            )
        ).perform(click())

        // Then the stories are ordered by 'top' and filtered by "to"
        verifyExpectedOrder(FILTER_SORT_TOP_INDICES)
    }

    /**
     * TODO: possibly this is flaky. Look into it
     *
     * Loops through a list of indices and checks that the starting
     * story associated with each index is in the correct order and
     * displayed in the list of stories.
     */
    private fun verifyExpectedOrder(expectedOrder: List<Int>) {
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedStory = STARTING_STORIES[expectedIndex]
            onView(withId(R.id.stories_list))
                .perform(RecyclerViewActions.scrollToPosition<StoryViewHolder>(listPosition))
            onView(withId(R.id.stories_list))
                .check(
                    matches(
                        allOf(
                            hasItemAtPosition(hasDescendant(withText(expectedStory.text)), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.rating.toString())), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.author)), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedStory.formattedDate())), listPosition)
                        )
                    )
                )
        }
    }

    companion object {
        private val FILTER_STRING = "Dogs"
        private val FILTER_AUTHOR = "Bob"
        private val FILTER_STRING_NO_MATCHES = "zzz"
    }

}
