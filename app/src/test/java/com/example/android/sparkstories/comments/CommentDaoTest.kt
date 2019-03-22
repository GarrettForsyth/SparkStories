package com.example.android.sparkstories.comments

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.data.CommentDao
import com.example.android.sparkstories.data.WriteItSayItHearItDatabase
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.repos.utils.WSHQueryHelper
import com.example.android.sparkstories.test.TestUtils.CHILD_COMMENT_ORDER
import com.example.android.sparkstories.test.TestUtils.COMMENT_SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.COMMENT_SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.COMMENT_SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.FIRST_STORY_COMMENT_ORDER
import com.example.android.sparkstories.test.TestUtils.SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.createTestComment
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.dataSourceFactoryToPagedList
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@LargeTest
@RunWith(AndroidJUnit4::class)
class CommentDaoTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())
    private val authors = dbSeed.SEED_AUTHORS
    private val cues = dbSeed.SEED_CUES
    private val stories = dbSeed.SEED_STORIES
    private val comments = dbSeed.SEED_COMMENTS

    private lateinit var commentDao: CommentDao
    private lateinit var db: WriteItSayItHearItDatabase

    @Before
    fun createAndSeedDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            WriteItSayItHearItDatabase::class.java
        ).allowMainThreadQueries().build()

        commentDao = db.commentDao()

        // seed with starting data
        db.authorDao().insert(authors)
        db.cueDao().insert(cues)
        db.storyDao().insert(stories)
        db.commentDao().insert(comments)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadComment() {
        val comment = createTestComment().apply {
            author = authors.first().name
            storyId = stories.first().id
        }
        val id = comments.size + 1
        commentDao.insert(comment)

        val readComment = commentDao.comment(id).getValueBlocking()
        assertTrue(readComment.text.equals(comment.text))
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCommentListWithSortOrderNew() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.NEW, _filterStoryId = 1)
        val query = WSHQueryHelper.comments(queryParameters)
        val readComments = dataSourceFactoryToPagedList(commentDao.comments(query), COMMENT_SORT_NEW_INDICES.size)

        val expectedStoryOrder = COMMENT_SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readComments)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCommentListWithSortOrderTop() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP, _filterStoryId = 1)
        val query = WSHQueryHelper.comments(queryParameters)
        val readComments = dataSourceFactoryToPagedList(commentDao.comments(query), COMMENT_SORT_TOP_INDICES.size)

        val expectedStoryOrder = COMMENT_SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readComments)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCommentListWithSortOrderHot() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.HOT, _filterStoryId = 1)
        val query = WSHQueryHelper.comments(queryParameters)
        val readComments = dataSourceFactoryToPagedList(commentDao.comments(query), COMMENT_SORT_HOT_INDICES.size)

        val expectedStoryOrder = COMMENT_SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readComments)
    }

    @Test
    @Throws(IOException::class)
    fun readAndUpdateComment() {
        val id = 1
        val readComment = commentDao.comment(id).getValueBlocking()
        readComment.rating = 100

        commentDao.update(readComment)
        val updatedComment = commentDao.comment(id).getValueBlocking()

        assertEquals(100, updatedComment.rating)
    }

    private fun assertCorrectOrder(expectedOrder: List<Int>, actualOrder: List<Comment>) {
        for (i in 0 until expectedOrder.size) {
            assert(comments[expectedOrder[i]].equals(actualOrder[i]))
        }
    }
}

