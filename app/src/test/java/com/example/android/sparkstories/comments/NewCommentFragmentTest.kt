package com.example.android.sparkstories.comments

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.R
import com.example.android.sparkstories.TestApp
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.model.comment.CommentTextField
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.StoryTextField
import com.example.android.sparkstories.stories.NewStoryFragmentTest
import com.example.android.sparkstories.test.TestUtils.createTestComment
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.test.TestUtils.createTestStory
import com.example.android.sparkstories.ui.comments.CommentsFragmentDirections
import com.example.android.sparkstories.ui.comments.NewCommentFragment
import com.example.android.sparkstories.ui.comments.NewCommentFragmentDirections
import com.example.android.sparkstories.ui.stories.NewStoryFragment
import com.example.android.sparkstories.ui.stories.NewStoryFragmentDirections
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.util.ViewModelUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertNull
import kotlinx.android.synthetic.main.fragment_new_comment.*
import kotlinx.android.synthetic.main.fragment_new_story.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class NewCommentFragmentTest {

    companion object {
        private val context = ApplicationProvider.getApplicationContext<Context>()
        private val MAXIMUM_COMMENT_LENGTH = CommentTextField.maxCharacters

        private val invalidCommentMessage = context.getString(
            R.string.invalid_new_comment_snackbar, MAXIMUM_COMMENT_LENGTH)

        private val STORY_ID_EXTRA = "storyId"
        private val STORY = createTestStory()

        private val PARENT_ID_EXTRA = "parentId"
        private val PARENT_COMMENT = createTestComment()
    }

    private var scenario: FragmentScenario<TestNewCommentFragment>

    // initialize the fragment with a cue id passed as an argument
    init {
        val args = Bundle()
        args.putString(STORY_ID_EXTRA, STORY.id)
        args.putInt(PARENT_ID_EXTRA, PARENT_COMMENT.id)
        scenario = launchInContainer(
            TestNewCommentFragment::class.java,
            args,
            TestNewCommentFragmentFactory()
        )
    }

    @Test
    fun submitCommentButton() {
        scenario.onFragment {
            it.submit_comment_button.performClick()
            verify(exactly = 1) { it.newCommentViewModel.onClickSubmit() }
        }
    }

    @Test
    fun showInvalidCommentSnackbar() {
        scenario.onFragment {
            it.invalidCommentSnackbar.value = Event(true)
        }
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidCommentMessage)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun showParentCommentButton() {
        scenario.onFragment {
            assert(ShadowAlertDialog.getShownDialogs().isEmpty())
            it.showParentComment.value = Event(true)
            assert(ShadowAlertDialog.getShownDialogs()[0].isShowing)
        }
    }

    @Test
    fun hideShowParentCommentButtonIfTopLevelComment() {
        scenario.onFragment {
            it.newCommentViewModel.getParentComment(-1)
            assert(!it.view_parent_comment_button.isShown)
        }
    }

    @Test
    fun showMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(true)
            assert(it.new_comment_top_menu.isShown)
        }
    }

    @Test
    fun hideMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(false)
            assertNull(it.new_comment_top_menu)
        }
    }

    @Test
    fun inPreviewMode() {
        scenario.onFragment {
            it.inPreviewMode.value = Event(true)
            assert(!it.new_comment_edit_text.isFocusableInTouchMode)
        }
    }

    @Test
    fun notInPreviewMode() {
        scenario.onFragment {
            it.inPreviewMode.value = Event(false)
            assert(it.new_comment_edit_text.isFocusableInTouchMode)
        }
    }

    @Test
    fun getComment() {
        scenario.onFragment {
            verify { it.newCommentViewModel.getParentComment(PARENT_COMMENT.id) }
        }
    }

    @Test
    fun setStoryId() {
        scenario.onFragment {
            verify { it.newCommentViewModel.setStoryId(STORY.id) }
        }
    }

    @Test
    fun shouldNavigateToComments() {
        scenario.onFragment {
            it.shouldNavigateToComments.value = Event(true)
            verify {
                it.navController.navigate(
                    NewCommentFragmentDirections.actionNewCommentFragmentToCommentsFragment(STORY.id)
                )
            }
        }
    }



    class TestNewCommentFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args)
                    as NewCommentFragmentTest.TestNewCommentFragment).apply {
                this.newCommentViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.newCommentViewModel)

                this.topMenuShown.value = Event(true)

                every { newCommentViewModel.invalidCommentSnackbar } returns invalidCommentSnackbar
                every { newCommentViewModel.shouldNavigateToComments } returns shouldNavigateToComments
                every { newCommentViewModel.topMenuStatus } returns topMenuShown
                every { newCommentViewModel.inPreviewMode } returns inPreviewMode
                every { newCommentViewModel.showParentComment } returns showParentComment
            }
        }
    }

    /**
     * Expose observed objects in test fragment. This allows the
     * tests to be driven by setting the value of these objects.
     *
     * i.e. setting the value to of these objects represents the
     * viewmodel forwarding an event or data change.
     */
    class TestNewCommentFragment : NewCommentFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController

        val invalidCommentSnackbar = MutableLiveData<Event<Boolean>>()
        val shouldNavigateToComments = MutableLiveData<Event<Boolean>>()
        val topMenuShown = MutableLiveData<Event<Boolean>>()
        val inPreviewMode = MutableLiveData<Event<Boolean>>()
        val showParentComment = MutableLiveData<Event<Boolean>>()
    }
}
