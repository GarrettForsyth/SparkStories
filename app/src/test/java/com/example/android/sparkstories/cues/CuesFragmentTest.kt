package com.example.android.sparkstories.cues

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.paging.PagedList
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.R
import com.example.android.sparkstories.TestApp
import com.example.android.sparkstories.databinding.CueListItemBinding
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.ui.cues.CuesFragment
import com.example.android.sparkstories.ui.cues.CuesFragmentDirections
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.util.ViewModelUtil
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.test.TestUtils.createTestCueList
import com.example.android.sparkstories.ui.common.DataBoundViewHolder
import com.example.android.sparkstories.util.InstantAppExecutors
import com.example.android.sparkstories.util.mockPagedList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_cues.*
import kotlinx.android.synthetic.main.fragment_new_screen_name.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*


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
            val cues = createTestCueList(5)
            it.liveResponseCues.postValue(Resource.success(mockPagedList(cues)))
            val expectedOrder = (0 until cues.size).toList()
            verifyInsideRecyclerView(expectedOrder, cues)
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
            it.liveResponseCues.value = Resource.success((mockPagedList(emptyList())))
            verify(exactly = 1) { it.cuesViewModel.setHasResults(false) }
        }
    }

    @Test
    fun progressBarShows_WhenLoadingCues() {
        scenario.onFragment {
            it.liveResponseCues.value = Resource.loading(null)
            assertTrue(it.progress_bar_cues.isShown)
        }
    }

    @Test
    fun progressBarHides_WhenSuccessfullyLoadedCues() {
        scenario.onFragment {
            it.liveResponseCues.value = Resource.loading(null)
            it.liveResponseCues.value = Resource.success(null)
            assertFalse(it.progress_bar_cues.isShown)
        }
    }

    @Test
    fun progressBarHides_WhenErrorLoadingCues() {
        scenario.onFragment {
            it.liveResponseCues.value = Resource.loading(null)
            it.liveResponseCues.value = Resource.error("", null)
            assertFalse(it.progress_bar_cues.isShown)
        }
    }


    // how to handle null
    @Test
    @Ignore
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
            it.liveResponseCues.value = Resource.success(mockPagedList(createTestCueList(5)))
            it.cues_list.children.first().callOnClick()
            verify(exactly = 1) { it.cuesViewModel.onClickCue(any()) }
        }
    }

    @Test
    fun navigateToCueFragmentWhenClickCueEventReceived() {
        scenario.onFragment {
            val id = UUID.randomUUID().toString()
            it.cueClicked.value = Event(id)
            verify {
                it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToCueFragment(id)
                )
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
            verify {
                it.navController.navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewCueFragment()
                )
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
                it.cuesViewModel.queryParameters.filterString = "d"
                it.cuesViewModel.queryParameters.filterString = "do"
                it.cuesViewModel.queryParameters.filterString = "dog"
                it.cuesViewModel.queryParameters.filterString = "dogs"
            }
        }
    }

    @Test
    fun selectingNewSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(0)
            verify { it.cuesViewModel.queryParameters.sortOrder = SortOrder.NEW }
        }
    }

    @Test
    fun selectingTopSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(1)
            verify { it.cuesViewModel.queryParameters.sortOrder = SortOrder.TOP }
        }
    }

    @Test
    fun selectingHotSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(2)
            verify { it.cuesViewModel.queryParameters.sortOrder = SortOrder.HOT }
        }
    }

    /**
     * Checks if each cue is inside the recyclerView
     */
    private fun verifyInsideRecyclerView(expectedOrder: List<Int>, cues: List<Cue>) {
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedCue = cues[expectedIndex]
            onView(withId(R.id.cues_list))
                .perform(RecyclerViewActions.scrollToPosition<DataBoundViewHolder<CueListItemBinding>>(listPosition))
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
                this.appExecutors = InstantAppExecutors()

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
        val liveResponseCues = MutableLiveData<Resource<PagedList<Cue>>>()
        val hasResults = MutableLiveData<Event<Boolean>>()
        val cueClicked = MutableLiveData<Event<String>>()
        val newCueButtonClick = MutableLiveData<Event<Boolean>>()
        override fun navController() = navController
    }

}
