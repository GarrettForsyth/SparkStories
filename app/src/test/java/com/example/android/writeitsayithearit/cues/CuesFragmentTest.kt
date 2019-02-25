package com.example.android.writeitsayithearit.cues

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.test.CustomMatchers.Companion.hasItemAtPosition
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_CUES
import com.example.android.writeitsayithearit.ui.cues.CuesFragment
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import com.example.android.writeitsayithearit.ui.cues.CuesFragmentDirections
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.SortOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_cues.*
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
    fun cuesAreObserved() {
        scenario.onFragment {
            verify(exactly = 1) { it.cuesViewModel.cues }
        }
    }

    @Test
    fun cuesAreInsideCueList() {
        scenario.onFragment {
            it.liveResponseCues.postValue(STARTING_CUES)
            val indices = (0 until STARTING_CUES.size).toList()
            verifyInsideRecyclerView(indices)
            verify(exactly = 1) { it.cuesViewModel.setHasResults(true) }
        }
    }

    @Test
    fun hasResultsStatusObserved() {
        scenario.onFragment {
            verify(exactly = 1) { it.cuesViewModel.hasResultsStatus }
        }
    }

    @Test
    fun setsZeroResultsOnEmptyList() {
        scenario.onFragment {
            it.liveResponseCues.value = (emptyList())
            verify(exactly = 1) { it.cuesViewModel.setHasResults(false) }
        }
    }

    @Test
    fun setsZeroResultsOnNull() {
        scenario.onFragment {
            it.liveResponseCues.value = null
            verify(exactly = 1) { it.cuesViewModel.setHasResults(false) }
        }
    }

    @Test
    fun showNoResultsTextViewWhenNoResults() {
        scenario.onFragment {
            it.hasResults.value = Event(false)
            assert(it.view!!.findViewById<TextView>(R.id.no_results).isShown)
        }
    }

    @Test
    fun hideNoResultsTextViewWhenResults() {
        scenario.onFragment {
            it.hasResults.value = Event(true)
            assert(!it.view!!.findViewById<TextView>(R.id.no_results).isShown)
        }
    }

    @Test
    fun clickCueEventSent() {
        scenario.onFragment {
            it.liveResponseCues.value = STARTING_CUES
            it.cues_list.children.first().callOnClick()
            verify(exactly = 1) { it.cuesViewModel.onClickCue(0) }
        }
    }

    @Test
    fun navigateToNewStoryFragmentWhenClickCueEventReceived() {
        scenario.onFragment {
            it.cueClicked.value = Event(0)
            verify {
                it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewStoryFragment(
                        0))
            }
        }
    }

    @Test
    fun clickNewCueEventSent() {
        scenario.onFragment {
            it.add_cue_fab.callOnClick()
            verify(exactly = 1) { it.cuesViewModel.onClickNewCue() }
        }
    }

    @Test
    fun navigateToNewCueFragmentWhenClickNewCueEventReceived() {
        scenario.onFragment {
            it.newCueButtonClick.value = Event(true)
            verify { it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewCueFragment())
            }
        }
    }

    @Test
    fun typingInFilterEditTextQueriesViewModel() {
        val filterString = "dogs"
        onView(withId(R.id.filter_cues_edit_text))
                .perform(typeText(filterString))
        scenario.onFragment {
            verify {
                it.cuesViewModel.filterQuery = "d"
                it.cuesViewModel.filterQuery = "do"
                it.cuesViewModel.filterQuery = "dog"
                it.cuesViewModel.filterQuery = "dogs"
            }
        }
    }

    @Test
    fun selectingNewSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(0)
            verify { it.cuesViewModel.sortOrder(SortOrder.NEW) }
        }
    }

    @Test
    fun selectingTopSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(1)
            verify { it.cuesViewModel.sortOrder(SortOrder.TOP) }
        }
    }

    @Test
    fun selectingHotSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(2)
            verify { it.cuesViewModel.sortOrder(SortOrder.HOT) }
        }
    }

        /**
         * Checks if each cue is inside the recyclerView
         */
        private fun verifyInsideRecyclerView(expectedOrder: List<Int>) {
            expectedOrder.forEachIndexed { listPosition, expectedIndex ->
                val expectedCue = STARTING_CUES[expectedIndex]
                onView(withId(R.id.cues_list))
                    .perform(RecyclerViewActions.scrollToPosition<CueViewHolder>(listPosition))
                onView(withId(R.id.cues_list))
                    .check(matches(hasDescendant(withText(expectedCue.text))))
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

                    every { cuesViewModel.cues } returns liveResponseCues
                    every { cuesViewModel.hasResultsStatus } returns hasResults
                    every { cuesViewModel.cueClicked } returns cueClicked
                    every { cuesViewModel.newCueFabClick } returns newCueButtonClick
                }
            }
        }

        /**
         * Overrides the nav controller to verify correct actions are
         * being called when expected.
         *
         * Also exposes the live data returned by the viewmodel as a
         * testing courtesy.
         */
        class TestCuesFragment : CuesFragment() {
            val navController: NavController = mockk(relaxed = true)
            val liveResponseCues = MutableLiveData<List<Cue>>()
            val hasResults = MutableLiveData<Event<Boolean>>()
            val cueClicked = MutableLiveData<Event<Int>>()
            val newCueButtonClick = MutableLiveData<Event<Boolean>>()
            override fun navController() = navController
        }

    }