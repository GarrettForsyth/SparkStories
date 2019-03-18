package com.example.android.writeitsayithearit.comments

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CommentDao
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.repos.CommentRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import com.example.android.writeitsayithearit.util.InstantAppExecutors
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class CommentRepositoryTest {

    private val dao: CommentDao = mockk(relaxed = true)
    private val commentRepository = CommentRepository(InstantAppExecutors(), dao)

    @Test
    fun loadCommentsLocally() {
       val queryParameters = QueryParameters(1,"", SortOrder.NEW)
        commentRepository.comments(queryParameters)
        verify(exactly = 1) { dao.comments(queryParameters.filterId, 0) }
    }

    @Test
    fun loadChildCommentsLocally() {
        commentRepository.childComments(1)
        verify(exactly = 1) { dao.childComments(1) }
    }
}
