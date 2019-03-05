package com.example.android.writeitsayithearit.stories

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.ui.stories.StoryViewHolder
import com.example.android.writeitsayithearit.ui.stories.StoriesFragment
import com.example.android.writeitsayithearit.ui.stories.StoriesFragmentDirections
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.test.TestUtils.createTestStoryList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_stories.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(
    application = TestApp::class
)
class StoriesFragmentTest {


    private val scenario = launchInContainer(
        TestStoriesFragment::class.java,
        null,
        TestStoriesFragmentFactory()
    )

    @Test
    fun storiesAreObserved() {
        scenario.onFragment {
            verify(exactly = 1) { it.storiesViewModel.stories }
        }
    }

    @Test
    fun storiesAreInsideStoryList() {
        scenario.onFragment {
            val stories = createTestStoryList(5)
            it.liveResponseStories.postValue(stories)
            val indices = (0 until stories.size).toList()
            verifyInsideRecyclerView(indices, stories)
            verify(exactly = 1) { it.storiesViewModel.setHasResults(true) }
        }
    }

    @Test
    fun hasResultsStatusObserved() {
        scenario.onFragment {
            verify(exactly = 1) { it.storiesViewModel.hasResultsStatus }
        }
    }

    @Test
    fun setsZeroResultsOnEmptyList() {
        scenario.onFragment {
            it.liveResponseStories.value = (emptyList())
            verify(exactly = 1) { it.storiesViewModel.setHasResults(false) }
        }
    }

    @Test
    fun setsZeroResultsOnNull() {
        scenario.onFragment {
            it.liveResponseStories.value = null
            verify(exactly = 1) { it.storiesViewModel.setHasResults(false) }
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
    fun clickStoryEventSent() {
        scenario.onFragment {
            val stories = createTestStoryList(5)
            it.liveResponseStories.value = stories
            it.stories_list.children.first().callOnClick()
            verify(exactly = 1) { it.storiesViewModel.onClickStory(0) }
        }
    }

    @Test
    fun navigateToStoryFragmentWhenClickStoryEventReceived() {
        scenario.onFragment {
            it.storyClicked.value = Event(0)
            verify {
                it.navController.navigate(
                    StoriesFragmentDirections.actionStoriesFragmentToStoryFragment(
                        0))
            }
        }
    }

    @Test
    fun typingInFilterEditTextQueriesViewModel() {
        val filterString = "dogs"
        onView(withId(R.id.filter_stories_edit_text))
            .perform(typeText(filterString))
        scenario.onFragment {
            verify {
                it.storiesViewModel.filterQuery = "d"
                it.storiesViewModel.filterQuery = "do"
                it.storiesViewModel.filterQuery = "dog"
                it.storiesViewModel.filterQuery = "dogs"
            }
        }
    }

    @Test
    fun selectingNewSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(0)
            verify { it.storiesViewModel.sortOrder(SortOrder.NEW) }
        }
    }

    @Test
    fun selectingTopSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(1)
            verify { it.storiesViewModel.sortOrder(SortOrder.TOP) }
        }
    }

    @Test
    fun selectingHotSortOrderQueriesViewModel() {
        scenario.onFragment {
            it.sort_order_spinner.setSelection(2)
            verify { it.storiesViewModel.sortOrder(SortOrder.HOT) }
        }
    }

    /**
     * Checks if each story is inside the recyclerView
     */
    private fun verifyInsideRecyclerView(expectedOrder: List<Int>, stories: List<Story>) {
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedStory = stories[expectedIndex]
            onView(withId(R.id.stories_list))
                .perform(RecyclerViewActions.scrollToPosition<StoryViewHolder>(listPosition))
            onView(withId(R.id.stories_list))
                .check(matches(hasDescendant(withText(expectedStory.text))))
        }
    }


    /**
     * A factory that returns a StoriesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     */
    class TestStoriesFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as TestStoriesFragment).apply {
                this.storiesViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.storiesViewModel)

                every { storiesViewModel.stories } returns liveResponseStories
                every { storiesViewModel.hasResultsStatus } returns hasResults
                every { storiesViewModel.storyClicked } returns storyClicked
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
    class TestStoriesFragment : StoriesFragment() {
        val navController: NavController = mockk(relaxed = true)
        val liveResponseStories = MutableLiveData<List<Story>>()
        val hasResults = MutableLiveData<Event<Boolean>>()
        val storyClicked = MutableLiveData<Event<Int>>()
        override fun navController() = navController
    }

}
