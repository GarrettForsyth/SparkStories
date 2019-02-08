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


    @Test
    fun userCreatesAValidCue() {
        onView(withId(R.id.add_cue_fab))
                .perform(click())

        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT))

        Espresso.closeSoftKeyboard()

        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        var endOfListItemPosition : Int? = 0
        scenario.onActivity {
            endOfListItemPosition = it.cues_list.adapter?.itemCount

        }

        Timber.d("There are ${endOfListItemPosition} items!")

        // new cue should be on the bottom
        onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.scrollToPosition<CueViewHolder>(
                        13
                ))

        onView(withText(NEW_CUE_TEXT))
                .check(matches(isDisplayed()))

    }

}