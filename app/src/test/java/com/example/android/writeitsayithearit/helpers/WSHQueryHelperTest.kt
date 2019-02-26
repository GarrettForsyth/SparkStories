package com.example.android.writeitsayithearit.helpers

import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.model.SortOrder
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class WSHQueryHelperTest {

    @Test
    fun queryAllCuesTest(){
        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY creation_date DESC"
        val query = WSHQueryHelper.cues().sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryCuesFilterTest() {
        val expectedSqlQuery = "SELECT  *  FROM cues WHERE text LIKE ? ORDER BY creation_date DESC"
        val query = WSHQueryHelper.cues(filterString = "updog").sql
        assertEquals(query, expectedSqlQuery)
    }

    @Test
    fun queryCuesSortNew() {
        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY creation_date DESC"
        val query = WSHQueryHelper.cues().sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryCuesSortTop() {
        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY rating DESC"
        val query = WSHQueryHelper.cues(sortOrder = SortOrder.TOP).sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryCuesSortHot() {
        val expectedSqlQuery = "SELECT  *  FROM cues WHERE creation_date  > ?  ORDER BY rating DESC"
        val query = WSHQueryHelper.cues(sortOrder = SortOrder.HOT).sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryCuesSortHotAndFilterTest() {
        val expectedSqlQuery = "SELECT  *  FROM cues WHERE text LIKE ? AND creation_date > ?  ORDER BY rating DESC"
        val query = WSHQueryHelper.cues(filterString = "updog", sortOrder = SortOrder.HOT).sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryAllStoriesTest(){
        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY creation_date DESC"
        val query = WSHQueryHelper.stories().sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryStoriesFilterTest() {
        val expectedSqlQuery = "SELECT  *  FROM stories WHERE text LIKE ? ORDER BY creation_date DESC"
        val query = WSHQueryHelper.stories(filterString = "updog").sql
        assertEquals(query, expectedSqlQuery)
    }

    @Test
    fun queryStoriesSortNew() {
        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY creation_date DESC"
        val query = WSHQueryHelper.stories().sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryStoriesSortTop() {
        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY rating DESC"
        val query = WSHQueryHelper.stories(sortOrder = SortOrder.TOP).sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryStoriesSortHot() {
        val expectedSqlQuery = "SELECT  *  FROM stories WHERE creation_date  > ?  ORDER BY rating DESC"
        val query = WSHQueryHelper.stories(sortOrder = SortOrder.HOT).sql
        assertEquals(query,expectedSqlQuery)
    }

    @Test
    fun queryStoriesSortHotAndFilterTest() {
        val expectedSqlQuery = "SELECT  *  FROM stories WHERE text LIKE ? AND creation_date > ?  ORDER BY rating DESC"
        val query = WSHQueryHelper.stories(filterString = "updog", sortOrder = SortOrder.HOT).sql
        assertEquals(query,expectedSqlQuery)
    }
}