package com.example.android.sparkstories.model.comment

/**
 * Object that defines the column and table names for the Comment model.
 */
object CommentContract {
    const val TABLE_NAME = "comments"
    const val COLUMN_ID = "id"
    const val COLUMN_TEXT = "text"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_STORY_ID = "story_id"
    const val COLUMN_PARENT_ID = "parent_id"
    const val COLUMN_DEPTH = "depth"
    const val COLUMN_CREATION_DATE = "creation_date"
    const val COLUMN_RATING = "rating"
}

