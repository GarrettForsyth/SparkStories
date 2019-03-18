package com.example.android.writeitsayithearit.comments

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.paging.PagedList
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.databinding.CommentListItemBinding
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.test.TestUtils.createTestCommentList
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.test.TestUtils.createTestStory
import com.example.android.writeitsayithearit.ui.comments.CommentsFragment
import com.example.android.writeitsayithearit.ui.common.DataBoundViewHolder
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.InstantAppExecutors
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.util.mockPagedList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(
    application = TestApp::class
)
class CommentsFragmentTest {

    companion object {
        private val STORY_ID_EXTRA = "story_id"
        private val STORY = createTestStory()
    }

    private var scenario : FragmentScenario<TestCommentsFragment>

    init {
        val args = Bundle()
        args.putInt(STORY_ID_EXTRA, STORY.id)
        scenario = launchInContainer(
            TestCommentsFragment::class.java,
            args,
            TestCommentsFragmentFactory()
        )
    }

    @Test
    fun storyIdIsSet() {
        scenario.onFragment {
            assertEquals(it.commentsViewModel.queryParameters.filterId, STORY.id)
        }
    }

    @Test
    fun commentsAreObserved() {
        scenario.onFragment {
            verify(exactly = 1) { it.commentsViewModel.comments }
        }
    }

    @Test
    fun commentsAreInsideCommentList() {
        scenario.onFragment {
            val comments = createTestCommentList(5)
            it.liveResponseComments.postValue(mockPagedList(comments))
            val indices = (0 until comments.size).toList()
            verifyInsideRecyclerView(indices, comments)
            verify(exactly = 1) { it.commentsViewModel.setHasResults(true) }
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
    fun showNoResultsTextViewWhenNoResults() {
        scenario.onFragment {
            it.hasResults.value = Event(false)
            assert(it.view!!.findViewById<TextView>(R.id.no_results).isShown)
        }
    }

    /**
     * Checks if each comment is inside the recyclerView
     */
    private fun verifyInsideRecyclerView(expectedOrder: List<Int>, comments: List<Comment>) {
        expectedOrder.forEachIndexed { listPosition, expectedIndex ->
            val expectedComment = comments[expectedIndex]
            onView(withId(R.id.comments_list))
                .perform(RecyclerViewActions.scrollToPosition<DataBoundViewHolder<CommentListItemBinding>>(listPosition))
            onView(withId(R.id.comments_list))
                .check(matches(hasDescendant(withText(expectedComment.text))))
        }
    }

    class TestCommentsFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(
                classLoader,
                className,
                args
            ) as CommentsFragmentTest.TestCommentsFragment).apply {
                this.commentsViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.commentsViewModel)
                this.appExecutors = InstantAppExecutors()

                every { commentsViewModel.comments } returns liveResponseComments
                every { commentsViewModel.hasResultsStatus } returns hasResults
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
    class TestCommentsFragment : CommentsFragment() {
        val navController: NavController = mockk(relaxed = true)
        val liveResponseComments = MutableLiveData<PagedList<Comment>>()
        val hasResults = MutableLiveData<Event<Boolean>>()
        override fun navController() = navController
    }
}

