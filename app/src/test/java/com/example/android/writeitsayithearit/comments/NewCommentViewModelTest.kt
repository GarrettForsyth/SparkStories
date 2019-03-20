package com.example.android.writeitsayithearit.comments

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.repos.CommentRepository
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.createTestComment
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.test.TestUtils.createTestStory
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.comments.NewCommentViewModel
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.writeitsayithearit.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.math.exp

@SmallTest
@RunWith(JUnit4::class)
class NewCommentViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val commentRepository: CommentRepository = mockk(relaxed = true)

    private lateinit var newCommentViewModel: NewCommentViewModel

    @Before
    fun init() {
        newCommentViewModel = NewCommentViewModel(commentRepository)
        newCommentViewModel.commentTextField = mockk(relaxed = true)
        newCommentViewModel.sharedPreferences = mockk(relaxed = true)

        mockObserverFor(newCommentViewModel.parentComment)
        mockObserverFor(newCommentViewModel.showParentComment)
        mockObserverFor(
            newCommentViewModel.topMenuStatus,
            newCommentViewModel.inPreviewMode,
            newCommentViewModel.invalidCommentSnackbar,
            newCommentViewModel.shouldNavigateToComments
        )
    }

    @Test
    fun getParentComment() {
        val parentComment = createTestComment()
        every { commentRepository.comment(parentComment.id) } returns parentComment.asLiveData()

        newCommentViewModel.getParentComment(parentComment.id)
        verify { commentRepository.comment(parentComment.id) }
        assertEquals(newCommentViewModel.parentComment.getValueBlocking(), parentComment)
    }

    @Test
    fun toggleMenu() {
        // initial state should be true
        assertTrue(newCommentViewModel.topMenuStatus.getValueBlocking().peekContent())

        newCommentViewModel.onToggleMenu()
        assertFalse(newCommentViewModel.topMenuStatus.getValueBlocking().peekContent())

        newCommentViewModel.onToggleMenu()
        assertTrue(newCommentViewModel.topMenuStatus.getValueBlocking().peekContent())
    }

    @Test
    fun togglePreviewMode() {
        // initial state should be false
        assertFalse(newCommentViewModel.inPreviewMode.getValueBlocking().peekContent())

        newCommentViewModel.onTogglePreviewMode()
        assertTrue(newCommentViewModel.inPreviewMode.getValueBlocking().peekContent())

        newCommentViewModel.onTogglePreviewMode()
        assertFalse(newCommentViewModel.inPreviewMode.getValueBlocking().peekContent())
    }

    @Test()
    fun showParentComment() {
        newCommentViewModel.onClickShowParentComment()

        assertFalse(
            newCommentViewModel
                .showParentComment
                .getValueBlocking()
                .hasBeenHandled
        )
    }


    @Test(expected = KotlinNullPointerException::class)
    fun navigateToCommentsFragmentInitiallyNull() {
        newCommentViewModel
            .shouldNavigateToComments
            .getValueBlocking()
    }

    @Test
    fun onClickSubmitWithNoParent() {
        // mock a valid story
        val expectedComment = createTestComment()
        newCommentViewModel.setStoryId(expectedComment.storyId)

        every { newCommentViewModel.commentTextField.isValid() } returns true
        every { newCommentViewModel.commentTextField.text } returns expectedComment.text
        every {
            newCommentViewModel.sharedPreferences
                .getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR)
        } returns expectedComment.author

        newCommentViewModel.onClickSubmit()
        verify { commentRepository.submitComment(expectedComment) }

        assertFalse(
            newCommentViewModel
                .shouldNavigateToComments
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test
    fun onClickSubmitWithParent() {
        // mock a valid story
        val expectedComment = createTestComment().apply { parentId = 52 }
        val parentComment = createTestComment().apply { id = 52; depth = 2 }
        every { commentRepository.comment(52) } returns parentComment.asLiveData()

        every { newCommentViewModel.commentTextField.isValid() } returns true
        every { newCommentViewModel.commentTextField.text } returns expectedComment.text
        every {
            newCommentViewModel.sharedPreferences.getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR)
        } returns expectedComment.author


        newCommentViewModel.setStoryId(expectedComment.storyId)
        newCommentViewModel.getParentComment(parentComment.id)
        newCommentViewModel.onClickSubmit()
        expectedComment.apply { depth = 3}
        verify { commentRepository.submitComment(expectedComment) }

        assertFalse(
            newCommentViewModel
                .shouldNavigateToComments
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test
    fun confirmInvalidSubmission() {
        val expectedComment = createTestComment()
        every { newCommentViewModel.commentTextField.isValid() } returns false

        newCommentViewModel.onClickSubmit()
        verify(exactly = 0) { commentRepository.submitComment(expectedComment) }

        assertNotNull(
            newCommentViewModel
                .invalidCommentSnackbar
                .getValueBlocking()
                .hasBeenHandled
        )
    }
}
