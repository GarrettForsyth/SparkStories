package com.example.android.writeitsayithearit.cues

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
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
import com.example.android.writeitsayithearit.ui.cues.NewCueFragment
import com.example.android.writeitsayithearit.ui.cues.NewCueFragmentDirections
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import io.mockk.Called
import io.mockk.mockk
import io.mockk.slot
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
class NewCueFragmentTest {

    companion object {
        private val context = ApplicationProvider.getApplicationContext<Context>()
        private val MINIMUM_CUE_LENGTH = context.resources.getInteger(R.integer.min_cue_text_length)
        private val MAXIMUM_CUE_LENGTH = context.resources.getInteger(R.integer.max_cue_text_length)

        private val NEW_CUE_TEXT_VALID = "a".repeat(MINIMUM_CUE_LENGTH)
        private val NEW_CUE_TEXT_TOO_SHORT = "a".repeat(MINIMUM_CUE_LENGTH - 1)
        private val NEW_CUE_TEXT_BLANK = " ".repeat(MINIMUM_CUE_LENGTH)
        private val NEW_CUE_TEXT_TOO_LONG = "a".repeat(MAXIMUM_CUE_LENGTH + 1)


        private val invalidCueMessage = context.getString(R.string.new_cue_invalid_message,
                MINIMUM_CUE_LENGTH, MAXIMUM_CUE_LENGTH)
    }


    private val scenario = launchInContainer(
            TestNewCueFragment::class.java,
            null,
            TestNewCueFragmentFactory()
    )

    @Test
    fun validCueCallsViewModel() {
        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT_VALID))


        val validCue = Cue(0, NEW_CUE_TEXT_VALID)

        onView(withId(R.id.submit_cue_btn))
                .perform(click())
        scenario.onFragment {
            verify(exactly =1) { it.newCueViewModel.submitCue(validCue) }
        }

        scenario.onFragment {
            verify(exactly = 1) { it.navController().navigate(
                    NewCueFragmentDirections.actionNewCueFragmentToCuesFragment()
            ) }
        }
    }

    @Test
    fun showInvalidWhenCueIsTooShort() {
        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT_TOO_SHORT))

        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCueMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenCueIsEmpty() {
        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCueMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenCueIsBlank() {
        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT_BLANK))

        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCueMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }

    @Test
    fun showInvalidWhenCueIsTooLong() {
        onView(withId(R.id.new_cue_edit_text))
                .perform(typeText(NEW_CUE_TEXT_TOO_LONG))

        onView(withId(R.id.submit_cue_btn))
                .perform(click())

        onView(allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCueMessage)
        )).check(matches(isDisplayed()))

        verifyNoNavigation()
    }


    @Test
    fun cueInfoIsShown() {
        onView(withId(R.id.new_cue_constraint_layout))
                .check(matches(hasDescendant(
                        withText(R.string.new_cue_info_text)
                )))
    }

    private fun verifyNoNavigation(){
        scenario.onFragment {
            verify(exactly = 0) { it.navController() wasNot Called }
        }
    }


    /**
     * A factory that returns a NewCuesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     */
    class TestNewCueFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args)
                    as TestNewCueFragment).apply {
                this.newCueViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.newCueViewModel)
            }
        }
    }

    /**
     * Simply overrides the nav controller to verify correct actions are
     * being called when expected.
     */
    class TestNewCueFragment : NewCueFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
    }

}