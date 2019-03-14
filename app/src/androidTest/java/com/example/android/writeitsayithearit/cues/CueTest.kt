package com.example.android.writeitsayithearit.cues

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.MainActivity
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.StoryListItemBinding
import com.example.android.writeitsayithearit.test.CustomMatchers.hasItemAtPosition
import com.example.android.writeitsayithearit.test.TestUtils.STORIES_FROM_FIRST_CUE_INDICES
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
import com.example.android.writeitsayithearit.ui.common.DataBoundViewHolder
import com.example.android.writeitsayithearit.util.CountingAppExecutorsRule
import com.example.android.writeitsayithearit.util.DataBindingIdlingResourceRule
import com.example.android.writeitsayithearit.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a reader
 * I want to view other stories written by a cue
 * And have the option to write a story from a cue
 * So I can enjoy reading them
 * And
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class CueTest {

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

    private val cue = dbSeed.SEED_CUES.first()

    @Before
    fun navigateToCuesFragment(){
        DataBindingIdlingResourceRule(scenarioRule)

        // And clicked on the first story
        onView(withText(cue.text)).perform(click())
    }

    @Test
    fun initialViewsVisible() {
        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                    allOf(
                        withText(cue.rating.toString()),
                        isDisplayed()
                    ))))

        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                allOf(
                    withText(cue.rating.toString()),
                    isDisplayed()
                ))))

        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                allOf(
                    withText(cue.text),
                    isDisplayed()
                ))))

        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                allOf(
                    withText(cue.author),
                    isDisplayed()
                ))))

        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                allOf(
                    withText(cue.formattedDate()),
                    isDisplayed()
                ))))

        onView(withId(R.id.cue_view))
            .check(matches(hasDescendant(
                allOf(
                    withText(cue.formattedTime()),
                    isDisplayed()
                ))))

        verifyExpectedOrder(STORIES_FROM_FIRST_CUE_INDICES)
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
                .perform(RecyclerViewActions.scrollToPosition<DataBoundViewHolder<StoryListItemBinding>>(listPosition))
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
