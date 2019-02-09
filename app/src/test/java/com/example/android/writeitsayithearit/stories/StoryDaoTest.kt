package com.example.android.writeitsayithearit.stories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.data.StoryDao
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
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

    private lateinit var storyDao: StoryDao
    private lateinit var db: WriteItSayItHearItDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                WriteItSayItHearItDatabase::class.java
        )
                .allowMainThreadQueries()
                .build()
        storyDao = db.storyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStory() {
        val story = Story(1, "Test Story", 1)
        storyDao.insert(story)
        val readStory = storyDao.story(1).getValueBlocking()
        assertTrue(readStory.text.equals(story.text))

    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadStoryList() {
        val stories = TestUtils.listOfStartingStories
        storyDao.insert(stories)
        val readStories = storyDao.stories().getValueBlocking()
        for (story in stories) {
            assert(readStories.contains(story))
        }

    }
}
