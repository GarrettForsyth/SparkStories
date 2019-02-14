package com.example.android.writeitsayithearit.cues

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to sort cues by ratings and creation date
 * So I can find good cues I haven't seen before
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class SortOrderCuesTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun launchActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    // Given the writer is starting at the main activity
    // And the database is seeded with 10 starting cues,
    // each with different ratings and creation dates
    @Test
    fun sortCuesByCreationDateTest() {
        // When the writer chooses order by 'new' on the spinner
        onView(withId(R.id.sort_order_spinner))
                .perform(click())
        onData(allOf(
                `is`(instanceOf(String::class.java)),
                `is`("New"))
        ).perform(click())

        // Then the list should be ordered by creation date
        val expectedCues = TestUtils.listOfStartingCues
        val numberOfStartingItems = expectedCues.size

        // starting cues were created from oldest to newst
        val newest = expectedCues.last()
        val oldest = expectedCues.first()

        // expected first view is visible
        onView(withText(newest.text)).check(matches(isDisplayed()))

        // scroll to last position
        onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.scrollToPosition<CueViewHolder>(
                        numberOfStartingItems - 1
                ))

        // expected last view is visible
        onView(withText(oldest.text)).check(matches(isDisplayed()))
    }
}