package com.example.android.writeitsayithearit.stories

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.cues.NewCueFragmentDirections
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragment
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragmentDirections
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(
        application = TestApp::class
)
class NewStoryFragmentTest {

    companion object {
        private val context = ApplicationProvider.getApplicationContext<Context>()
        private val MINIMUM_STORY_LENGTH = context.resources.getInteger(R.integer.min_story_text_length)
        private val MAXIMUM_STORY_LENGTH = context.resources.getInteger(R.integer.max_story_text_length)

        private val NEW_STORY_TEXT_VALID = "a".repeat(MINIMUM_STORY_LENGTH)
        private val NEW_STORY_TEXT_TOO_SHORT = "a".repeat(MINIMUM_STORY_LENGTH - 1)
        private val NEW_STORY_TEXT_BLANK = " ".repeat(MINIMUM_STORY_LENGTH)
        private val NEW_STORY_TEXT_TOO_LONG = "a".repeat(MAXIMUM_STORY_LENGTH + 1)


        private val invalidStoryMessage = context.getString(R.string.new_story_invalid_message,
                MINIMUM_STORY_LENGTH, MAXIMUM_STORY_LENGTH)

        private val CUE_ID_EXTRA = "cue_id"
        private val CUE = TestUtils.listOfStartingCues.last()
    }

    private var scenario: FragmentScenario<TestNewStoryFragment>

    init {
        val args = Bundle()
        args.putInt(CUE_ID_EXTRA, CUE.id)
        scenario = launchInContainer(
                NewStoryFragmentTest.TestNewStoryFragment::class.java,
                args,
                NewStoryFragmentTest.TestNewStoryFragmentFactory()
        )

    }

    @Test
    fun validStoryCallsViewModel() {
        onView(withId(R.id.new_story_edit_text))
                .perform(typeText(NEW_STORY_TEXT_VALID))


        val validStory = Story(0, NEW_STORY_TEXT_VALID, 0)

        onView(withId(R.id.submit_story_btn))
                .perform(click())

        scenario.onFragment {
            verify(exactly =1) { it.newStoryViewModel.submitStory(validStory) }
        }

        scenario.onFragment {
            verify(exactly = 1) { it.navController().navigate(
                    NewStoryFragmentDirections.actionNewStoryFragmentToStoriesFragment()
            ) }
        }
    }

    @Test
    fun showInvalidWhenStoryIsTooShort() {
        onView(withId(R.id.new_story_edit_text))
                .perform(typeText(NEW_STORY_TEXT_TOO_SHORT))

        onView(withId(R.id.submit_story_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidStoryMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenStoryIsEmpty() {
        onView(withId(R.id.submit_story_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidStoryMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenStoryIsBlank() {
        onView(withId(R.id.new_story_edit_text))
                .perform(typeText(NEW_STORY_TEXT_BLANK))

        onView(withId(R.id.submit_story_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidStoryMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenStoryIsTooLong() {
        onView(withId(R.id.new_story_edit_text))
                .perform(typeText(NEW_STORY_TEXT_TOO_LONG))

        onView(withId(R.id.submit_story_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidStoryMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun cueIsShown() {
        onView(withId(R.id.new_story_constraint_layout))
                .check(matches(hasDescendant(
                        withText(CUE.text)
                )))
    }

    @Test
    fun storyInfoIsShown() {
        onView(withId(R.id.new_story_constraint_layout))
                .check(matches(hasDescendant(
                        withText(R.string.new_story_info_text)
                )))
    }

    private fun verifyNoNavigation(){
        scenario.onFragment {
            verify(exactly = 0) { it.navController() wasNot Called }
        }
    }

    class TestNewStoryFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args)
                    as TestNewStoryFragment).apply {
                this.newStoryViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.newStoryViewModel)

                val liveCue = MutableLiveData<Cue>()
                liveCue.value = CUE
                every { newStoryViewModel.cue(CUE.id) } returns liveCue
            }
        }
    }

    class TestNewStoryFragment : NewStoryFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
    }

}