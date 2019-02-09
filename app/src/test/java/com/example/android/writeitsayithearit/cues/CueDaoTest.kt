package com.example.android.writeitsayithearit.cues

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.vo.Cue
import junit.framework.Assert.assertTrue
import org.junit.After
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

    private lateinit var cueDao: CueDao
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
        cueDao = db.cueDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCue() {
        val cue = Cue(1, "Test Cue")
        cueDao.insert(cue)
        val readCue = cueDao.cue(1).getValueBlocking()
        assertTrue(readCue.text.equals(cue.text))

    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadCueList() {
        val cues = TestUtils.listOfStartingCues
        cueDao.insert(cues)
        val readCues = cueDao.cues().getValueBlocking()
        for (cue in cues) {
            assert(readCues.contains(cue))
        }

    }
}