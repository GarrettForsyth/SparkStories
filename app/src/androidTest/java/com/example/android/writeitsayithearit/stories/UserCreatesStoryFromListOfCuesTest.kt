package com.example.android.writeitsayithearit.stories

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.vo.Cue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 *
 * As a writer
 * I want to create a story and share it with community
 * When I find a cue that inspires me
 *
 */
@RunWith(AndroidJUnit4::class)
class UserCreatesStoryFromListOfCuesTest {

    companion object {
        private val STORY_TEXT = "This is a new story! Enjoy."
    }

    private lateinit var scenario : ActivityScenario<MainActivity>

    private val cues: List<Cue> = TestUtils.listOfStartingCues

    @Before
    fun launchActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    // Given the writer is starting at the main activity
    // And the data is seeded with 10 starting cues
    @Test
    fun userCreatesStoryFromCue() {
        // When the user clicks the first starting cue
        onView(withText(cues.first().text))
                .perform(click())

        // And enters their story into edit text
        onView(withId(R.id.new_story_edit_text))
                .perform(typeText(STORY_TEXT))

        // And clicks submit
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submit_story_btn))
                .perform(click())

        // Then they should see their new story at the top of the list of stories

//        var endOfListItemPosition : Int = 0
//        scenario.onActivity {
//            endOfListItemPosition = it.stories_list.adapter?.itemCount!! - 1
//
//        }
//
//        onView(withId(R.id.stories_list))
//                .perform(RecyclerViewActions.scrollToPosition<StoryViewHolder>(
//                        endOfListItemPosition
//                ))
//
        onView(withText(STORY_TEXT))
                .check(matches(isDisplayed()))

    }
}