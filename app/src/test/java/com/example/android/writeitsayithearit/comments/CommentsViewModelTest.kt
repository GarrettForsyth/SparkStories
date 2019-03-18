package com.example.android.writeitsayithearit.comments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.repos.CommentRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.ui.comments.CommentsViewModel
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import com.example.android.writeitsayithearit.util.MockUtils.mockObserverFor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
        commentsViewModel.queryParameters.filterId = 1
        val expectedQueryParameters = QueryParameters(1, "", SortOrder.NEW)
        verify { commentRepository.comments(expectedQueryParameters) }
    }

    @Test
    fun getChildComments() {
        commentsViewModel.childComments(1)
        verify { commentRepository.childComments(1) }
    }
}

