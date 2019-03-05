package com.example.android.writeitsayithearit.stories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.test.TestUtils.SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_HOT_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_NEW_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_SORT_TOP_INDICES
import com.example.android.writeitsayithearit.test.TestUtils.STORY_FILTER_TEXT
import com.example.android.writeitsayithearit.test.TestUtils.createTestStory
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
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
        val readStories = storyDao.stories(
            WSHQueryHelper.stories()).getValueBlocking()

        for (stories in stories) {
            assert(readStories.contains(stories))
        }
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithFilter() {
        val query = WSHQueryHelper.stories(STORY_FILTER_TEXT)
        val readStories = storyDao.stories(query).getValueBlocking()
        assert(readStories.size == 3)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithSortOrderNew() {
        val query = WSHQueryHelper.stories("", SortOrder.NEW)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithTop() {
        val query = WSHQueryHelper.stories("", SortOrder.TOP)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHot() {
        val query = WSHQueryHelper.stories("", SortOrder.HOT)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHotWithFilter() {
        val query = WSHQueryHelper.stories(STORY_FILTER_TEXT, SortOrder.HOT)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = STORY_FILTER_SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithTopWithFilter() {
        val query = WSHQueryHelper.stories(STORY_FILTER_TEXT, SortOrder.TOP)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = STORY_FILTER_SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithNewWithFilter() {
        val query = WSHQueryHelper.stories(STORY_FILTER_TEXT, SortOrder.NEW)
        val readStories = storyDao.stories(query).getValueBlocking()

        val expectedStoryOrder = STORY_FILTER_SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    private fun assertCorrectOrder(expectedOrder: List<Int>, actualOrder: List<Story>) {
        for (i in 0 until expectedOrder.size) {
            assert(stories[expectedOrder[i]].equals(actualOrder[i]))
        }
    }

}
