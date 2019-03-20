package com.example.android.writeitsayithearit.repos.utils

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.example.android.writeitsayithearit.model.cue.CueContract
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.comment.CommentContract
import com.example.android.writeitsayithearit.model.story.StoryContract
import com.example.android.writeitsayithearit.ui.util.QueryParameters
import java.util.*

///**
// * This helper class is a shortcut for creating SupportSQLiteQuery's for the
// * [CueDao] and [StoryDao] . It supports filtering by text and sort order.
// */
//object WSHQueryHelper {

//    fun cues(
//        filterString: String = "",
//        sortOrder: SortOrder = SortOrder.NEW,
//        cueId: Int = -1
//    ): SupportSQLiteQuery {
//        val queryBuilder = SupportSQLiteQueryBuilder.builder(CueContract.TABLE_NAME)
//        addSelection(queryBuilder, filterString, sortOrder, cueId)
//        addSortOrder(queryBuilder, sortOrder)
//        return queryBuilder.create()
//    }

//    fun stories(
//        filterString: String = "",
//        sortOrder: SortOrder = SortOrder.NEW,
//        cueId: Int = -1
//    ): SupportSQLiteQuery {
//        val queryBuilder = SupportSQLiteQueryBuilder.builder(StoryContract.TABLE_NAME)
//        addSelection(queryBuilder, filterString, sortOrder, cueId)
//        addSortOrder(queryBuilder, sortOrder)
//        return queryBuilder.create()
//    }

//    fun comments(queryParameters: QueryParameters): SupportSQLiteQuery? {
//        return SupportSQLiteQueryBuilder.builder(CommentContract.TABLE_NAME).create()
//    }

//    private fun addSelection(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        filterString: String,
//        sortOrder: SortOrder,
//        cueId: Int
//    ) {

//        if (sortOrder == SortOrder.HOT) {
//            val millisInADay = 86400000
//            val yesterdayInMillis = Calendar.getInstance().timeInMillis - millisInADay

//            if (filterString.isBlank()) {
//                if (cueId == -1) {
//                    addHotSelection(queryBuilder, yesterdayInMillis)
//                } else {
//                    addHotSelectionFilterCue(queryBuilder, yesterdayInMillis, cueId)
//                }
//            } else {
//                if (cueId == -1) {
//                    addFilterAndHotSelection(queryBuilder, filterString, yesterdayInMillis)
//                } else {
//                    addFilterAndHotSelectionFilterCue(queryBuilder, filterString, yesterdayInMillis, cueId)
//                }
//            }

//        } else {
//            if (!filterString.isBlank()) {
//                if (cueId == -1) {
//                    addFilterSelection(queryBuilder, filterString)
//                } else {
//                    addFilterSelectionFilterCue(queryBuilder, filterString, cueId)
//                }
//            } else {
//                if (cueId != -1) {
//                    addFilterCue(queryBuilder, cueId)
//                }
//            }
//        }
//    }

//    private fun addFilterCue(queryBuilder: SupportSQLiteQueryBuilder, cueId: Int) {
//        queryBuilder.selection(
//            "${StoryContract.COLUMN_CUE_ID} == ? ",
//            arrayOf(cueId)
//        )
//    }

//    private fun addFilterSelection(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        filterString: String
//    ) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_TEXT} LIKE ? OR ${CueContract.COLUMN_AUTHOR} LIKE ?",
//            arrayOf("%$filterString%", "%$filterString%")
//        )
//    }

//    private fun addFilterSelectionFilterCue(queryBuilder: SupportSQLiteQueryBuilder, filterString: String, cueId: Int) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_TEXT} LIKE ? OR ${CueContract.COLUMN_AUTHOR} LIKE ? AND ${StoryContract.COLUMN_CUE_ID} == ? ",
//            arrayOf("%$filterString%", "%$filterString%", cueId)
//        )
//    }

//    private fun addFilterAndHotSelection(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        filterString: String, yesterdayInMillis: Long
//    ) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_TEXT} LIKE ? AND ${CueContract.COLUMN_CREATION_DATE} > ? ",
//            arrayOf("%$filterString%", yesterdayInMillis)
//        )
//    }

//    private fun addFilterAndHotSelectionFilterCue(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        filterString: String,
//        yesterdayInMillis: Long,
//        cueId: Int
//    ) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_TEXT} LIKE ? AND ${CueContract.COLUMN_CREATION_DATE} > ? AND ${StoryContract.COLUMN_CUE_ID} == ? ",
//            arrayOf("%$filterString%", yesterdayInMillis, cueId)
//        )
//    }

//    private fun addHotSelection(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        yesterdayInMillis: Long
//    ) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_CREATION_DATE}  > ? ",
//            arrayOf(yesterdayInMillis)
//        )
//    }

//    private fun addHotSelectionFilterCue(queryBuilder: SupportSQLiteQueryBuilder, yesterdayInMillis: Long, cueId: Int) {
//        queryBuilder.selection(
//            "${CueContract.COLUMN_CREATION_DATE}  > ? AND ${StoryContract.COLUMN_CUE_ID} == ? ",
//            arrayOf(yesterdayInMillis, cueId)
//        )
//    }

//    private fun addSortOrder(
//        queryBuilder: SupportSQLiteQueryBuilder,
//        sortOrder: SortOrder
//    ) {
//        if (sortOrder == SortOrder.NEW) {
//            queryBuilder.orderBy("${CueContract.COLUMN_CREATION_DATE} DESC")
//        } else {
//            queryBuilder.orderBy("${CueContract.COLUMN_RATING} DESC")

//        }
//    }
//}

