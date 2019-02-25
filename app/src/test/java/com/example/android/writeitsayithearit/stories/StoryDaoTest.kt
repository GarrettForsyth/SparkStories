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
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.vo.SortOrder
import com.example.android.writeitsayithearit.vo.Story
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

    private lateinit var storiesDao: StoryDao
    private lateinit var db: WriteItSayItHearItDatabase
    private val stories = TestUtils.STARTING_STORIES

    @Before
    fun createAndSeedDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                WriteItSayItHearItDatabase::class.java
        ).allowMainThreadQueries().build()

        storiesDao = db.storyDao()

        // seed with starting data
        storiesDao.insert(stories)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStory() {
        val story = TestUtils.createTestStory()
        val id = stories.size + 1
        storiesDao.insert(story)

        val readStory = storiesDao.story(id).getValueBlocking()
        assertTrue(readStory.text.equals(story.text))
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryList() {
        val readStories = storiesDao.stories(WSHQueryHelper.stories()).getValueBlocking()
        for (stories in stories) {
            assert(readStories.contains(stories))
        }
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithFilter() {
        val query = WSHQueryHelper.stories("Dogs")
        val readStories = storiesDao.stories(query).getValueBlocking()
        assert(readStories.size == 1)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithSortOrderNew() {
        val query = WSHQueryHelper.stories("", SortOrder.NEW)
        val readStories = storiesDao.stories(query).getValueBlocking()

        val expectedStoryOrder = TestUtils.SORT_NEW_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithTop() {
        val query = WSHQueryHelper.stories("", SortOrder.TOP)
        val readStories = storiesDao.stories(query).getValueBlocking()

        val expectedStoryOrder = TestUtils.SORT_TOP_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHot() {
        val query = WSHQueryHelper.stories("", SortOrder.HOT)
        val readStories = storiesDao.stories(query).getValueBlocking()

        val expectedStoryOrder = TestUtils.SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryListWithHotWithFilter() {
        val query = WSHQueryHelper.stories("to", SortOrder.HOT)
        val readStories = storiesDao.stories(query).getValueBlocking()

        val expectedStoryOrder = TestUtils.FILTER_SORT_HOT_INDICES
        assertCorrectOrder(expectedStoryOrder, readStories)
    }

    private fun assertCorrectOrder(expectedOrder: List<Int>, actualOrder: List<Story>) {
        for (i in 0 until expectedOrder.size) {
            assert(stories[expectedOrder[i]].equals(actualOrder[i]))
        }
    }

}
