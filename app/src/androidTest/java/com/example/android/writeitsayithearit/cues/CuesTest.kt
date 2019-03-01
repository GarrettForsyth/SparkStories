package com.example.android.writeitsayithearit.cues

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.test.CustomMatchers.Companion.hasItemAtPosition
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_AUTHOR_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.FILTER_SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_CUES
import com.example.android.writeitsayithearit.ui.cues.CueViewHolder
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import kotlinx.android.synthetic.main.fragment_cues.*
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to browse a list of cues
 * To find a cue that inspires me to write
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class CuesTest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun launchApp() {
        // Given I have launched the app
        // And the database is seeded with 10 starting cues,
        //  one of which contains the text 'Dogs'
        //  each with different ratings and creation dates
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun startingCuesAreDisplayed() {
        // Then the list of starting cues are displayed in the cues list
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun userFiltersByCueText() {
        // When I type 'Dogs' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
            .perform(typeText(FILTER_STRING))

        // Then the starting list should filter everything but the
        // one starting cue containing the word dogs
        scenario.onActivity {
            assert(it.cues_list.adapter?.itemCount!! == 1)
        }

        val expectedText = STARTING_CUES[6].text
        onView(withId(R.id.cues_list))
            .check(matches(hasDescendant(withText(expectedText))))
    }

    @Test
    fun userFiltersByCueAuthor() {
        // When I type 'Bob' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
            .perform(typeText(FILTER_AUTHOR))

        // Then the starting list should filter everything but the
        // three  starting cue containing the author Bob
        scenario.onActivity {
            assert(it.cues_list.adapter?.itemCount!! == 3)
        }

        verifyExpectedOrder(FILTER_AUTHOR_NEW_INDICES)
    }

    @Test
    fun noResults() {
        // When I type 'zzz' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
            .perform(typeText(FILTER_STRING_NO_MATCHES))

        // Then the no results text view is shown
        onView(withId(R.id.no_results)).check(matches(isDisplayed()))

        // When I type 'Dogs' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
            .perform(replaceText(""))
            .perform(typeText(FILTER_STRING))

        // Then the no results text view is hidden
        onView(withId(R.id.no_results)).check(matches(not(isDisplayed())))
    }

    @Test
    fun sortCuesByNew() {
        // When I chooses order by 'new' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("New")
            )
        ).perform(click())

        // Thent he cues are sorted by their creation date
        verifyExpectedOrder(SORT_NEW_INDICES)
    }

    @Test
    fun sortCuesByTop() {
        // When I chooses order by 'top' on the spinner
        onView(withId(R.id.sort_order_spinner))
            .perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Top")
            )
        ).perform(click())

        // Then the cues are sorted by rating
        verifyExpectedOrder(SORT_TOP_INDICES)
    }

    @Test
    fun sortCuesByHot() {
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
        // cues from the last day
        verifyExpectedOrder(SORT_HOT_INDICES)
    }

    @Test
    fun sortCuesByHotAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
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
    fun sortCuesByNewAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
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

        // Then the cues are ordered by 'new' and filtered by 'to'
        verifyExpectedOrder(FILTER_SORT_NEW_INDICES)
    }

    @Test
    fun sortCuesByTopAndFilter() {
        // When I type 'to' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
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

        // Then the cues are ordered by 'top' and filtered by "to"
        verifyExpectedOrder(FILTER_SORT_TOP_INDICES)
    }

    /**
     * Loops through a list of indices and checks that the starting
     * cue associated with each index is in the correct order and
     * displayed in the list of cues.
     */
    private fun verifyExpectedOrder(expectedOrder: List<Int>) {
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedCue = STARTING_CUES[expectedIndex]
            onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.scrollToPosition<CueViewHolder>(listPosition))

            onView(withId(R.id.cues_list))
                .check(
                    matches(
                        allOf(
                            hasItemAtPosition(hasDescendant(withText(expectedCue.text)), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedCue.rating.toString())), listPosition),
                            hasItemAtPosition(hasDescendant(withText(expectedCue.author)), listPosition)
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