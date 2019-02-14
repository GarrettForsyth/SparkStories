package com.example.android.writeitsayithearit.stories

import android.os.Bundle
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
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.adapters.vh.StoryViewHolder
import com.example.android.writeitsayithearit.ui.stories.StoriesFragment
import com.example.android.writeitsayithearit.ui.stories.StoriesFragmentDirections
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_cues.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class StoriesFragmentTest {

    private val scenario = launchInContainer(
            TestStoriesFragment::class.java,
            null,
            TestStoriesFragmentFactory()
    )

    @Test
    fun resultsAreInsideRecyclerView() {
        val stories = TestUtils.listOfStartingStories

        // check first story is in hierarchy
        onView(withId(R.id.stories_list))
                .check(matches(hasDescendant(
                        withText(stories.first().text))
                ))

        // Scroll to position last position
        onView(ViewMatchers.withId(R.id.stories_list))
                .perform(
                        RecyclerViewActions.scrollToPosition<StoryViewHolder>(stories.size - 1)
                )

        // check the last story in the list is in the hierarchy
        onView(withId(R.id.stories_list))
                .check(matches(hasDescendant(
                        withText(stories.last().text))
                ))
    }

    @Test
    fun clickingStoryNavigatesToStoryFragment() {
        val story = TestUtils.listOfStartingStories.first()

        onView(withId(R.id.stories_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<StoryViewHolder>(
                        0, click()
                ))

        scenario.onFragment {
            verify { it.navController.navigate(
                    StoriesFragmentDirections.actionStoriesFragmentToStoryFragment(
                            story.id
                    )
            )
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
                it.storiesViewModel.filterQuery("d")
                it.storiesViewModel.filterQuery("do")
                it.storiesViewModel.filterQuery("dog")
                it.storiesViewModel.filterQuery("dogs")
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
     * A factory that returns a StoriesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     *
     * TODO: factor this out to a test utility
     */
    class TestStoriesFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as TestStoriesFragment).apply {
                this.storiesViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.storiesViewModel)

                val liveStories = MutableLiveData<List<Story>>()
                liveStories.postValue(TestUtils.listOfStartingStories)
                every { storiesViewModel.stories } returns liveStories
            }
        }
    }

    /**
     * Simply overrides the nav controller to verify correct actions are
     * being called when expected.
     */
    class TestStoriesFragment : StoriesFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
    }
}
