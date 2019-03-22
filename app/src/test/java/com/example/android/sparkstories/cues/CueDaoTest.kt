package com.example.android.sparkstories.cues

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.data.CueDao
import com.example.android.sparkstories.data.WriteItSayItHearItDatabase
import com.example.android.sparkstories.repos.utils.WSHQueryHelper
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.test.TestUtils.CUE_FILTER_SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.CUE_FILTER_SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.CUE_FILTER_SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.CUE_FILTER_TEXT
import com.example.android.sparkstories.test.TestUtils.SORT_HOT_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_NEW_INDICES
import com.example.android.sparkstories.test.TestUtils.SORT_TOP_INDICES
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.ui.util.QueryParameters
import com.example.android.sparkstories.util.dataSourceFactoryToPagedList
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@LargeTest
@RunWith(AndroidJUnit4::class)
class CueDaoTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())
    private val cues = dbSeed.SEED_CUES
    private val authors = dbSeed.SEED_AUTHORS

    private lateinit var cueDao: CueDao
    private lateinit var db: WriteItSayItHearItDatabase


    @Before
    fun createAndSeedDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                WriteItSayItHearItDatabase::class.java
        ).allowMainThreadQueries().build()

        cueDao = db.cueDao()

        // seed with starting data
        db.authorDao().insert(authors)
        cueDao.insert(cues)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCue() {
        val cue = createTestCue().apply {
            author = authors.first().name
        }
        cueDao.insert(cue)

        val id = cues.size + 1
        val readCue = cueDao.cue(id).getValueBlocking()

        assertTrue(readCue.text.equals(cue.text))
    }

    @Test
    @Throws(IOException::class)
    fun readAndUpdateCue() {
        val id = 1
        val readCue = cueDao.cue(id).getValueBlocking()
        readCue.rating = 100

        cueDao.update(readCue)
        val updatedCue = cueDao.cue(id).getValueBlocking()

        assertEquals(100, updatedCue.rating)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueList() {
        val readCues = dataSourceFactoryToPagedList(
            cueDao.cues(WSHQueryHelper.cues(QueryParameters())), cues.size)
        for (cue in cues) {
            assert(readCues.contains(cue))
        }
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithFilter() {
        val queryParameters = QueryParameters(_filterString = CUE_FILTER_TEXT)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(
            cueDao.cues(query), cues.size)
        assert(readCues.size == 6)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithSortOrderNew() {
        val queryParameters = QueryParameters()
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = SORT_NEW_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithTop() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = SORT_TOP_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithHot() {
        val queryParameters = QueryParameters(_sortOrder = SortOrder.HOT)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = SORT_HOT_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithHotWithFilter() {
        val queryParameters = QueryParameters(_filterString = CUE_FILTER_TEXT,_sortOrder = SortOrder.HOT)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = CUE_FILTER_SORT_HOT_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithTopWithFilter() {
        val queryParameters = QueryParameters(_filterString = CUE_FILTER_TEXT,_sortOrder = SortOrder.TOP)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = CUE_FILTER_SORT_TOP_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueListWithNewWithFilter() {
        val queryParameters = QueryParameters(_filterString = CUE_FILTER_TEXT,_sortOrder = SortOrder.NEW)
        val query = WSHQueryHelper.cues(queryParameters)
        val readCues = dataSourceFactoryToPagedList(cueDao.cues(query), cues.size)

        val expectedCueOrder = CUE_FILTER_SORT_NEW_INDICES
        assertCorrectOrder(expectedCueOrder, readCues)
    }

    private fun assertCorrectOrder(expectedOrder: List<Int>, actualOrder: List<Cue>) {
        for (i in 0 until expectedOrder.size) {
            assert(cues[expectedOrder[i]].equals(actualOrder[i]))
        }
    }


}