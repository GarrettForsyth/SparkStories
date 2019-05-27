package com.example.android.sparkstories.repos.utils

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.model.comment.CommentContract
import com.example.android.sparkstories.model.cue.CueContract
import com.example.android.sparkstories.model.story.StoryContract
import com.example.android.sparkstories.ui.util.QueryParameters
import java.lang.StringBuilder
import java.util.*

object SparkStoriesQueryHelper {

    fun cues(queryParameters: QueryParameters): SupportSQLiteQuery {
        val queryBuilder = SupportSQLiteQueryBuilder.builder(CueContract.TABLE_NAME)

        val selection = StringBuilder("")
        val selectionArgs = Array(2) { "" }

        selection.append("(text LIKE ? OR author LIKE ?)")
        selectionArgs[0] = ("%${queryParameters.filterString}%")
        selectionArgs[1] = ("%${queryParameters.filterString}%")

        appendHotSelection(queryParameters, selection)

        return queryBuilder.selection(selection.toString(), selectionArgs)
            .orderBy(getSortOrder(queryParameters.sortOrder))
            .create()
    }

    fun stories(queryParameters: QueryParameters): SupportSQLiteQuery {
        val queryBuilder = SupportSQLiteQueryBuilder.builder(StoryContract.TABLE_NAME)

        val selection = StringBuilder("")
        val selectionArgs = Array(2) { "" }

        selection.append("(text LIKE ? OR author LIKE ?)")
        selectionArgs[0] = ("%${queryParameters.filterString}%")
        selectionArgs[1] = ("%${queryParameters.filterString}%")

        if (queryParameters.filterCueId.isNotBlank()) {
            selection.append(" AND ${StoryContract.COLUMN_CUE_ID} = '${queryParameters.filterCueId}'")
        }

        appendHotSelection(queryParameters, selection)

        return queryBuilder.selection(selection.toString(), selectionArgs)
            .orderBy(getSortOrder(queryParameters.sortOrder))
            .create()

    }

    fun comments(queryParameters: QueryParameters): SupportSQLiteQuery {
        val queryBuilder = SupportSQLiteQueryBuilder.builder(CommentContract.TABLE_NAME)

        val selection = StringBuilder("")
        val selectionArgs = Array(2) { "" }

        selection.append("(text LIKE ? OR author LIKE ?)")
        selectionArgs[0] = ("%${queryParameters.filterString}%")
        selectionArgs[1] = ("%${queryParameters.filterString}%")

        if (queryParameters.filterStoryId.isNotBlank()) {
            selection.append(" AND ${CommentContract.COLUMN_STORY_ID} = '${queryParameters.filterStoryId}'")
            println(" AND ${CommentContract.COLUMN_STORY_ID} = '${queryParameters.filterStoryId}'")
        }

        if (queryParameters.filterParentCommentId > 0) {
            selection.append(" AND ${CommentContract.COLUMN_PARENT_ID} = ${queryParameters.filterParentCommentId}")
        }else {
            selection.append(" AND ${CommentContract.COLUMN_PARENT_ID} = -1")
        }

        appendHotSelection(queryParameters, selection)

        return queryBuilder.selection(selection.toString(), selectionArgs)
            .orderBy(getSortOrder(queryParameters.sortOrder))
            .create()
    }

    private fun getSortOrder(sortOrder: SortOrder): String {
        return when (sortOrder) {
            SortOrder.NEW -> {
                "creation_date DESC"
            }
            SortOrder.HOT -> {
                "rating DESC"
            }
            SortOrder.TOP -> {
                "rating DESC"
            }
        }

    }

    private fun appendHotSelection(
        queryParameters: QueryParameters,
        selection: StringBuilder
    ) {
        if (queryParameters.sortOrder == SortOrder.HOT) {
            val millisInADay = 86400000
            val yesterdayInMillis = Calendar.getInstance().timeInMillis - millisInADay
            selection.append(" AND creation_date > $yesterdayInMillis")
        }
    }
}
