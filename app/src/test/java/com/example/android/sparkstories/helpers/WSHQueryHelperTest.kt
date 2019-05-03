package com.example.android.sparkstories.helpers

//@SmallTest
//@RunWith(JUnit4::class)
//class WSHQueryHelperTest {
//
//    @Test
//    fun queryAllCuesTest(){
//        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.cues(QueryParameters()).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryCuesFilterTest() {
//        val expectedSqlQuery = "SELECT  *  FROM cues WHERE (text LIKE ? OR author LIKE ?) ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.cues(QueryParameters(_filterString = "updog")).sql
//        assertEquals(query, expectedSqlQuery)
//    }
//
//    @Test
//    fun queryCuesSortNew() {
//        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.cues(QueryParameters(_sortOrder = SortOrder.NEW)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryCuesSortTop() {
//        val expectedSqlQuery = "SELECT  *  FROM cues ORDER BY rating DESC"
//        val query = WSHQueryHelper.cues(QueryParameters(_sortOrder = SortOrder.TOP)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryCuesSortHot() {
//        val expectedSqlQuery = "SELECT  *  FROM cues WHERE creation_date  > ?  ORDER BY rating DESC"
//        val query = WSHQueryHelper.cues(QueryParameters(_sortOrder = SortOrder.HOT)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryCuesSortHotAndFilter() {
//        val expectedSqlQuery = "SELECT  *  FROM cues WHERE text LIKE ? AND creation_date > ?  ORDER BY rating DESC"
//        val queryParameters = QueryParameters(_filterString = "updog", _sortOrder = SortOrder.HOT)
//        val query = WSHQueryHelper.cues(queryParameters).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryAllStories(){
//        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.stories(QueryParameters()).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesFilter() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE text LIKE ? OR author LIKE ? ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_filterString = "updog")).sql
//        assertEquals(query, expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE cue_id == ?  ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_filterCueId = 1)).sql
//        assertEquals(query, expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesFilterAndFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE text LIKE ? OR author LIKE ? AND cue_id == ?  ORDER BY creation_date DESC"
//        val queryParameters = QueryParameters(_filterString = "updog", _filterCueId = 1)
//        val query = WSHQueryHelper.stories(queryParameters).sql
//        assertEquals(query, expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortNew() {
//        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_sortOrder = SortOrder.NEW)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortNewAndFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE cue_id == ?  ORDER BY creation_date DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_filterCueId = 1, _sortOrder = SortOrder.NEW)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortTop() {
//        val expectedSqlQuery = "SELECT  *  FROM stories ORDER BY rating DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_sortOrder = SortOrder.TOP)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortTopAndFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE cue_id == ?  ORDER BY rating DESC"
//        val queryParameters = QueryParameters(_sortOrder = SortOrder.TOP, _filterCueId = 1 )
//        val query = WSHQueryHelper.stories(queryParameters).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortHot() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE creation_date  > ?  ORDER BY rating DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_sortOrder = SortOrder.HOT)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortHotAndFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE creation_date  > ? AND cue_id == ?  ORDER BY rating DESC"
//        val query = WSHQueryHelper.stories(QueryParameters(_filterCueId = 1, _sortOrder = SortOrder.HOT)).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortHotAndFilterTest() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE text LIKE ? AND creation_date > ?  ORDER BY rating DESC"
//        val queryParameters = QueryParameters(_filterString = "updog", _sortOrder = SortOrder.HOT)
//        val query = WSHQueryHelper.stories(queryParameters).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//
//    @Test
//    fun queryStoriesSortHotAndFilterAndFilterCue() {
//        val expectedSqlQuery = "SELECT  *  FROM stories WHERE (text LIKE ? OR author LIKE ?) AND creation_date > 1552944059511 ORDER BY rating DESC"
//        val queryParameters = QueryParameters(_filterCueId = 1, _filterString = "updog", _sortOrder = SortOrder.HOT)
//        val query = WSHQueryHelper.stories(queryParameters).sql
//        assertEquals(query,expectedSqlQuery)
//    }
//}