package com.example.android.writeitsayithearit.repos.utils

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.example.android.writeitsayithearit.model.cue.CueContract
import com.example.android.writeitsayithearit.model.SortOrder
import com.example.android.writeitsayithearit.model.story.StoryContract
import java.util.*

/**
 * This helper class is a shortcut for creating SupportSQLiteQuery's for the
 * [CueDao] and [StoryDao] . It supports filtering by text and sort order.
 */
object WSHQueryHelper {

    fun cues(
            filterString: String = "",
            sortOrder: SortOrder = SortOrder.NEW
    ): SupportSQLiteQuery {
        val queryBuilder = SupportSQLiteQueryBuilder.builder(CueContract.TABLE_NAME)
        addSelection(queryBuilder, filterString, sortOrder)
        addSortOrder(queryBuilder, sortOrder)
        return queryBuilder.create()
    }

    fun stories(
            filterString: String = "",
            sortOrder: SortOrder = SortOrder.NEW
    ): SupportSQLiteQuery {
        val queryBuilder = SupportSQLiteQueryBuilder.builder(StoryContract.TABLE_NAME)
        addSelection(queryBuilder, filterString, sortOrder)
        addSortOrder(queryBuilder, sortOrder)
        return queryBuilder.create()
    }

    private fun addSelection(
            queryBuilder: SupportSQLiteQueryBuilder,
            filterString: String,
            sortOrder: SortOrder
    ) {

        if (sortOrder == SortOrder.HOT) {
            val millisInADay = 86400000
            val yesterdayInMillis = Calendar.getInstance().timeInMillis - millisInADay

            if (filterString.isBlank()) {
                addHotSelection(queryBuilder, yesterdayInMillis)
            } else {
                addFilterAndHotSelection(queryBuilder, filterString, yesterdayInMillis)
            }

        } else {
            if (!filterString.isBlank()) {
                addFilterSelection(queryBuilder, filterString)
            }
        }
    }

    private fun addFilterSelection(
            queryBuilder: SupportSQLiteQueryBuilder,
            filterString: String
    ) {
        queryBuilder.selection(
                "${CueContract.COLUMN_TEXT} LIKE ?",
                arrayOf("%$filterString%")
        )
    }

    private fun addFilterAndHotSelection(
            queryBuilder: SupportSQLiteQueryBuilder,
            filterString: String, yesterdayInMillis: Long
    ) {
        queryBuilder.selection(
                "${CueContract.COLUMN_TEXT} LIKE ? AND ${CueContract.COLUMN_CREATION_DATE} > ? ",
                arrayOf("%$filterString%", yesterdayInMillis)
        )
    }

    private fun addHotSelection(
            queryBuilder: SupportSQLiteQueryBuilder,
            yesterdayInMillis: Long
    ) {
        queryBuilder.selection(
                "${CueContract.COLUMN_CREATION_DATE}  > ? ",
                arrayOf(yesterdayInMillis)
        )
    }

    private fun addSortOrder(
            queryBuilder: SupportSQLiteQueryBuilder,
            sortOrder: SortOrder
    ) {
        if (sortOrder == SortOrder.NEW) {
            queryBuilder.orderBy("${CueContract.COLUMN_CREATION_DATE} DESC")
        } else {
            queryBuilder.orderBy("${CueContract.COLUMN_RATING} DESC")

        }
    }
}