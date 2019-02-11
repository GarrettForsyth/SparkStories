package com.example.android.writeitsayithearit.cues

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.cues.CuesFragment
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import com.example.android.writeitsayithearit.ui.cues.CuesFragmentDirections
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(
        application = TestApp::class
        )
class CuesFragmentTest {


    private val scenario = launchInContainer(
            TestCuesFragment::class.java,
            null,
            TestCuesFragmentFactory()
    )

    @Test
    fun fragmentQueriesViewModelForCues() {
        scenario.onFragment {
            verify(exactly = 1) { it.cuesViewModel.cues }
        }
    }

    /**
     * Can't check whether views are displayed using robolectric,
     * so check if they exist in the view hierarchy instead.
     *
     * The views will only exist in the hierarchy if they are (at least almost)
     * on the screen. So the recyclerview must be scrolled to check for
     * views that wouldn't normally be on the screen.
     *
     * To keep the test quick, only the first and last cues are checked
     */
    @Test
    fun resultsAreInsideRecyclerView() {
        val cues = TestUtils.listOfStartingCues

        // check first cue is in hierarchy
        onView(withId(R.id.cues_list))
                .check(matches(hasDescendant(
                        withText(cues.first().text))
                ))

        // Scroll to position last position
        onView(ViewMatchers.withId(R.id.cues_list))
                .perform(
                        RecyclerViewActions.scrollToPosition<CueViewHolder>(cues.size - 1)
                )

        // check the last cue in the list is in the hierarchy
        onView(withId(R.id.cues_list))
                .check(matches(hasDescendant(
                        withText(cues.last().text))
                ))
    }

    @Test
    fun clickingAddCueButtonNavigatesToNewCueFragment() {
        onView(withId(R.id.add_cue_fab))
                .perform(click())
        scenario.onFragment {
            verify { it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewCueFragment())
            }
        }
    }

    @Test
    fun clickingCueNavigatesToNewStoryFragment() {
        val cue = TestUtils.listOfStartingCues.first()

        onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<CueViewHolder>(
                        0, click()
                ))

        scenario.onFragment {
            verify { it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewStoryFragment(
                            cue.id
                    )
            )
            }
        }
    }

    /**
     * A factory that returns a CuesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     */
    class TestCuesFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as TestCuesFragment).apply {
                this.cuesViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.cuesViewModel)

                val liveCues = MutableLiveData<List<Cue>>()
                liveCues.postValue(TestUtils.listOfStartingCues)
                every { cuesViewModel.cues } returns liveCues
            }
        }
    }

    /**
     * Simply overrides the nav controller to verify correct actions are
     * being called when expected.
     */
    class TestCuesFragment : CuesFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
    }

}