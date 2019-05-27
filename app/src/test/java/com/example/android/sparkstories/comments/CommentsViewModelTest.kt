package com.example.android.sparkstories.comments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.repos.comment.CommentRepository
import com.example.android.sparkstories.test.TestUtils.createTestComment
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.comments.CommentsViewModel
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@SmallTest
@RunWith(JUnit4::class)
class CommentsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val commentRepository: CommentRepository = mockk(relaxed = true)

    private lateinit var commentsViewModel: CommentsViewModel

    @Before
    fun init() {
        commentsViewModel = CommentsViewModel(commentRepository)

        mockObserverFor(commentsViewModel.comments)
        mockObserverFor(commentsViewModel.hasResultsStatus)
    }

    @Test
    fun getComments() {
        val storyId = UUID.randomUUID().toString()
        commentsViewModel.queryParameters.filterStoryId = storyId
        val expectedQueryParameters = QueryParameters(_filterStoryId = storyId)
        verify { commentRepository.comments(expectedQueryParameters) }
    }

    @Test
    fun getChildComments() {
        commentsViewModel.childComments(1)
        verify { commentRepository.childComments(1) }
    }

    @Test
    fun onNewCommentButtonClick(){
        commentsViewModel.onClickNewComment(-1)
        assertNotNull(commentsViewModel.shouldNavigateToNewComment.getValueBlocking().peekContent())
    }

    @Test
    fun sortByNew() {
        commentsViewModel.queryParameters.sortOrder = SortOrder.NEW
        val expectedParameters = QueryParameters()
        verify { commentRepository.comments(expectedParameters) }
    }

    @Test
    fun sortByTop() {
        commentsViewModel.queryParameters.sortOrder = SortOrder.TOP
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        verify { commentRepository.comments(expectedParameters) }
    }

    @Test
    fun sortByHot() {
        commentsViewModel.queryParameters.sortOrder = SortOrder.HOT
        val expectedParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        verify { commentRepository.comments(expectedParameters) }
    }

    @Test
    fun clickLikeCommentButton() {
        val comment = createTestComment()

        commentsViewModel.onClickLikeComment(comment)
        val expectedComment = comment.copy().apply { rating++ }
        verify { commentRepository.update(expectedComment) }
    }
}

