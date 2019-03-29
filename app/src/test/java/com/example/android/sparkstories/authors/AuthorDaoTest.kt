package com.example.android.sparkstories.authors

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.data.local.AuthorDao
import com.example.android.sparkstories.data.local.WriteItSayItHearItDatabase
import com.example.android.sparkstories.test.TestUtils
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.test.getValueBlocking
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthorDaoTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())

    private lateinit var authorDao: AuthorDao
    private lateinit var db: WriteItSayItHearItDatabase
    private val authors = dbSeed.SEED_AUTHORS

    @Before
    fun createAndSeedDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            WriteItSayItHearItDatabase::class.java
        ).allowMainThreadQueries().build()

        authorDao = db.authorDao()

        // seed with starting data
        authorDao.insert(authors)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadAuthor() {
        val author = TestUtils.createTestAuthor()
        authorDao.insert(author)

        val readAuthor = authorDao.author(author.name).getValueBlocking()
        assertTrue(readAuthor.name.equals(author.name))
    }

    @Test
    @Throws(IOException::class)
    fun writeAndReadAuthorList() {
        val readAuthors = authorDao.allAuthors().getValueBlocking()
        for (author in authors) {
            assert(readAuthors.contains(author))
        }
    }
}
