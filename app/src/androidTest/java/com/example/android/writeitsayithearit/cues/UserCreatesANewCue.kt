package com.example.android.writeitsayithearit.cues

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import kotlinx.android.synthetic.main.fragment_cues.*
import timber.log.Timber


/**
 * As a user
 * I want to create cues for the community
 * To help writers create original stories
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class UserCreatesANewCue {

    companion object {
        private val NEW_CUE_TEXT = "this is a new test cue"
    }

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Before
    fun launchActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }


    // Given the user is starting at the main activity
    // And the database is seeded with 10 starting cues
    @Test
    fun userCreatesAValidCue() {
        // When the user clicks the + floating fab
        onView(withId(R.id.add_cue_fab))
                .perform(click())

        // And enters their cue into the edit text
        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT))

        // And presses the submit button
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        // Then they should see their new cue on the bottom list of cues
        var endOfListItemPosition : Int = 0
        scenario.onActivity {
            endOfListItemPosition = it.cues_list.adapter?.itemCount!!
            Timber.d("TEST" + endOfListItemPosition.toString())

        }
        onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.scrollToPosition<CueViewHolder>(
                        endOfListItemPosition
                ))

        onView(withText(NEW_CUE_TEXT))
                .check(matches(isDisplayed()))
    }



}