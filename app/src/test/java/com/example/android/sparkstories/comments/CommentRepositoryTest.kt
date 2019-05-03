package com.example.android.sparkstories.comments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.data.local.CommentDao
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.repos.comment.CommentRepository
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.test.TestUtils.createTestComment
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.InstantAppExecutors
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CommentRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dao: CommentDao = mockk(relaxed = true)
    private val wshQueryHelper: SparkStoriesQueryHelper = mockk(relaxed = true)
    private val commentRepository =
        CommentRepository(InstantAppExecutors(), dao, wshQueryHelper)

    @Test
    fun submitComment() {
        val comment = createTestComment()
        commentRepository.submitComment(comment)
        verify(exactly = 1) { dao.insert(comment) }
    }

    @Test
    fun loadComment() {
        val commentId = 12
        commentRepository.comment(commentId)
        verify(exactly = 1) { dao.comment(commentId) }
    }

    @Test
    fun loadCommentsLocally() {
        val queryParameters = QueryParameters(_filterStoryId = 1)
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.comments(queryParameters) } returns mockedQuery
        commentRepository.comments(queryParameters)
        verify(exactly = 1) { dao.comments(mockedQuery) }
    }

    @Test
    fun loadLocallyOrderByNew() {
        val queryParameters = QueryParameters(_filterStoryId = 1)
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.comments(queryParameters) } returns mockedQuery

        commentRepository.comments(queryParameters)
        verify(exactly = 1) { dao.comments(mockedQuery) }

    }

    @Test
    fun loadLocallyOrderByTop() {
        val queryParameters = QueryParameters(_filterStoryId = 1, _sortOrder = SortOrder.TOP)
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.comments(queryParameters) } returns mockedQuery

        commentRepository.comments(queryParameters)
        verify(exactly = 1) { dao.comments(mockedQuery) }
    }

    @Test
    fun loadLocallyOrderByHot() {
        val queryParameters = QueryParameters(_filterStoryId = 1, _sortOrder = SortOrder.HOT)
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.comments(queryParameters) } returns mockedQuery

        commentRepository.comments(queryParameters)
        verify(exactly = 1) { dao.comments(mockedQuery) }
    }

    @Test
    fun loadChildCommentsLocally() {
        val queryParameters = QueryParameters(_filterParentCommentId = 1)
        val mockedQuery: SupportSQLiteQuery = mockk()
        every { wshQueryHelper.comments(queryParameters) } returns mockedQuery

        commentRepository.childComments(1)
        verify(exactly = 1) { dao.comments(mockedQuery) }
    }

    @Test
    fun updateComment() {
        val comment = createTestComment()
        commentRepository.update(comment)
        verify(exactly = 1) { dao.update(comment) }
    }
}
