package com.example.android.sparkstories.stories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.data.local.StoryDao
import com.example.android.sparkstories.data.local.WriteItSayItHearItDatabase
import com.example.android.sparkstories.repos.utils.WSHQueryHelper
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.test.TestUtils.SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.STORY_FILTER_SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.STORY_FILTER_SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.STORY_FILTER_SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.STORY_FILTER_TEXT
import com.example.android.sparkstories.test.TestUtils.createTestStory
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.dataSourceFactoryToPagedList
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@LargeTest
@RunWith(AndroidJUnit4::class)
class StoryDaoTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())
    private val authors = dbSeed.SEED_AUTHORS
    private val cues = dbSeed.SEED_CUES
    private val stories = dbSeed.SEED_STORIES

    private lateinit var storyDao: StoryDao
    private lateinit var db: WriteItSayItHearItDatabase

    @Before
    fun createAndSeedDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            WriteItSayItHearItDatabase::class.java
        ).allowMainThreadQueries().build()

        storyDao = db.storyDao()

        // seed with starting data
        db.authorDao().insert(authors)
        db.cueDao().insert(cues)
        storyDao.insert(stories)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStory() {
        val story = createTestStory().apply {
            author = authors.first().name
        }
        val id = stories.size + 1
        storyDao.insert(story)

        val readStory = storyDao.story(id).getValueBlocking()
        assertTrue(readStory.text.equals(story.text))
    }

    @Test
    @Throws(IOException::class)
    fun readAndUpdateStory() {
        val id = 1
        val readStory = storyDao.story(id).getValueBlocking()
        readStory.rating = 100

        storyDao.update(readStory)
        val updatedStory = storyDao.story(id).getValueBlocking()

        assertEquals(100, updatedStory.rating)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryList() {
        val query = WSHQueryHelper.stories(QueryParameters())
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        for (stories in stories) {
            assert(readStories.contains(stories))
        }
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithFilter() {
        val queryParameters = QueryParameters(_filterString = STORY_FILTER_TEXT)
        val query = WSHQueryHelper.stories(queryParameters)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)
        assert(readStories.size == 3)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithSortOrderNew() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.NEW)
        val query = WSHQueryHelper.stories(queryParameters)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        val expectedStoryOrder = SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithTop() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        val query = WSHQueryHelper.stories(queryParameters)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        val expectedStoryOrder = SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHot() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        val query = WSHQueryHelper.stories(queryParameters)
        println(query.sql)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)
        val expectedStoryOrder = SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHotWithFilter() {
        val queryParameters = QueryParameters(_filterString = STORY_FILTER_TEXT, _sortOrder = SortOrder.HOT)
        val query = WSHQueryHelper.stories(queryParameters)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        val expectedStoryOrder = STORY_FILTER_SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithTopWithFilter() {
        val queryParameters = QueryParameters(_filterString = STORY_FILTER_TEXT, _sortOrder = SortOrder.TOP)
        val query = WSHQueryHelper.stories(queryParameters)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        val expectedStoryOrder = STORY_FILTER_SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithNewWithFilter() {
        val queryParameters = QueryParameters(_filterString = STORY_FILTER_TEXT, _sortOrder = SortOrder.NEW)
        val query = WSHQueryHelper.stories(queryParameters)
        println(query.sql)
        val readStories = dataSourceFactoryToPagedList(storyDao.stories(query), stories.size)

        val expectedStoryOrder = STORY_FILTER_SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    private fun assertCorrectOrder(expectedOrder: List<Int>, actualOrder: List<Story>) {
        println(expectedOrder)
        println(actualOrder.map { story -> story.id -1 })
        for (i in 0 until expectedOrder.size) {
            assert(stories[expectedOrder[i]].equals(actualOrder[i]))
        }
    }

}
