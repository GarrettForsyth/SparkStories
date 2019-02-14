package com.example.android.writeitsayithearit.cues

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.TestUtils
import com.google.common.base.Predicates.equalTo
import kotlinx.android.synthetic.main.fragment_cues.*
import kotlinx.android.synthetic.main.fragment_stories.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to filter the list of cues
 * To find cues of my interest to me
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class FilterCuesTest {

    companion object {
        private val FILTER_STRING = "Dogs"
    }

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Before
    fun launchActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    // Given the writer is starting at the main activity
    // And the database is seeded with 10 starting cues,
    // one of which contains the text 'Dogs'
    @Test
    fun userFiltersByCueText() {
        // When the user types 'Dogs' into the filter edit text
        onView(withId(R.id.filter_cues_edit_text))
                .perform(typeText(FILTER_STRING))

        // Then the starting list should filter everything but the
        // one starting cue containing the word dogs
        scenario.onActivity {
            assert(it.cues_list.adapter?.itemCount!! == 1)
        }

        val expectedText = TestUtils.listOfStartingCues[6].text
        onView(withId(R.id.cues_list))
                .check(matches(hasDescendant(withText(expectedText))))
    }
}