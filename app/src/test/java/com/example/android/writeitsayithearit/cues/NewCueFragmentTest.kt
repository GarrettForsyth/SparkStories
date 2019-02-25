package com.example.android.writeitsayithearit.cues

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
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
import com.example.android.writeitsayithearit.ui.cues.NewCueFragment
import com.example.android.writeitsayithearit.ui.cues.NewCueFragmentDirections
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragmentDirections
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_new_cue.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class NewCueFragmentTest {

    companion object {
        private val context = ApplicationProvider.getApplicationContext<Context>()
        private val MINIMUM_CUE_LENGTH = context.resources.getInteger(R.integer.min_cue_text_length)
        private val MAXIMUM_CUE_LENGTH = context.resources.getInteger(R.integer.max_cue_text_length)

        private val invalidCueMessage = context.getString(R.string.invalid_new_cue_snackbar,
                MINIMUM_CUE_LENGTH, MAXIMUM_CUE_LENGTH)
    }


    private val scenario = launchInContainer(
            TestNewCueFragment::class.java,
            null,
            TestNewCueFragmentFactory()
    )

    @Test
    fun submitCueButton() {
        scenario.onFragment {
            it.submit_cue_btn.callOnClick()
            verify(exactly = 1) { it.newCueViewModel.onClickSubmitCue() }
        }
    }

    @Test
    fun showInvalidStorySnackbar() {
        scenario.onFragment {
            it.invalidCueSnackBar.value = Event(true)
        }
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCueMessage)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNavigateToCues() {
        scenario.onFragment {
            it.shouldNavigateToCues.value = Event(true)
            verify {
                it.navController.navigate(
                    NewCueFragmentDirections.actionNewCueFragmentToCuesFragment())
            }
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

                every { newCueViewModel.shouldNavigateToCues } returns shouldNavigateToCues
                every { newCueViewModel.invalidCueSnackBar } returns invalidCueSnackBar
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

        val invalidCueSnackBar = MutableLiveData<Event<Boolean>>()
        val shouldNavigateToCues = MutableLiveData<Event<Boolean>>()
    }

}