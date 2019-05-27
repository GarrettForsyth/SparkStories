package com.example.android.sparkstories.test

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.test.core.app.ApplicationProvider
import com.example.android.sparkstories.R
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.Story
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

/**
 * Some predefined data and helper functions for testing.
 */
object TestUtils {

    /**
     * The expected orderings for each sort order.
     */
    val SORT_NEW_INDICES = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val SORT_TOP_INDICES = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    val SORT_HOT_INDICES = listOf(5, 4, 3, 2, 1, 0)

    /**
     * The expected cue orderings for each sort order with a filter query
     */
    val CUE_FILTER_TEXT = "ip"
    val CUE_FILTER_SORT_HOT_INDICES = listOf(4, 0)
    val CUE_FILTER_SORT_NEW_INDICES = listOf(0, 4, 6, 7, 8, 10)
    val CUE_FILTER_SORT_TOP_INDICES = listOf(10, 8, 7, 6, 4, 0)

    val CUE_FILTER_AUTHOR = "Rose"
    val CUE_FILTER_AUTHOR_NEW_INDICES = listOf(8, 9)

    /**
     * The expected story orderings for each sort order with a filter query
     */
    val STORY_FILTER_TEXT = "vivamus a"
    val STORY_FILTER_SORT_HOT_INDICES = listOf(1)
    val STORY_FILTER_SORT_NEW_INDICES = listOf(1, 6, 7)
    val STORY_FILTER_SORT_TOP_INDICES = listOf(7, 6, 1)

    val STORY_FILTER_AUTHOR = "Cremin"
    val STORY_FILTER_AUTHOR_NEW_INDICES = listOf(6, 7)

    val FILTER_STRING_NO_MATCHES = "zzz"

    val STORIES_FROM_FIRST_CUE_INDICES = listOf(0, 1)

    val FIRST_STORY_COMMENT_ORDER = listOf(0, 1, 2)
    val CHILD_COMMENT_ORDER = listOf(6, 9, 10) // for id = 6

    val COMMENT_SORT_NEW_INDICES = listOf(0,1,2,11)
    val COMMENT_SORT_TOP_INDICES = listOf(0,11,1,2)
    val COMMENT_SORT_HOT_INDICES = listOf(0,1,2)

    fun createTestCue() = Cue(
        text = "Test cue text. Very interesting stuff.",
        author = "Test Cue Author",
        creationDate = 0,
        rating = 0
    )

    fun createTestCueList(n: Int): List<Cue> {
        val cues: MutableList<Cue> = mutableListOf()
        for (i in 0..n) {
            cues.add(
                Cue(
                    text = "Text for cue $i. This is the text.",
                    author = "Author for cue $i",
                    creationDate = 0,
                    rating = 0
                )
            )
        }
        return cues
    }

    fun createTestStory() = Story(
        text = "This is a test story. It was the best of stories, it was the worst of stories.",
        author = "Test Story Author",
        cueId = UUID.randomUUID().toString(),
        creationDate = 0,
        rating = 0
    )

    fun createTestComment() = Comment(
        id = 0,
        storyId = UUID.randomUUID().toString(),
        parentId = -1,
        depth = 0,
        text = "This is a test comment!",
        author = "Test Comment Author",
        creationDate = 0,
        rating = 0
    )

    fun createTestCommentList(n: Int): List<Comment> {
        val comments: MutableList<Comment> = mutableListOf()
        for (i in 0 until n) {
            comments.add(
                Comment(
                    id = 0,
                    storyId = UUID.randomUUID().toString(),
                    parentId = -1,
                    depth = 0,
                    text = "This is test comment $i!",
                    author = "Test Comment Author $i",
                    creationDate = 0,
                    rating = 0
                )
            )
        }
        return comments
    }


    fun createTestStoryList(n: Int): List<Story> {
        val stories: MutableList<Story> = mutableListOf()
        for (i in 0 until n) {
            stories.add(
                Story(
                    text = "Text for cue $i. This is the text.",
                    author = "Author for cue $i",
                    cueId = UUID.randomUUID().toString(),
                    creationDate = 0,
                    rating = 0
                )
            )
        }
        return stories
    }

    fun createTestAuthor() = Author(name = "Test Author")

    fun createTestAuthorList(n: Int): List<Author> {
        val authors: MutableList<Author> = mutableListOf()
        for (i in 0..n) {
            authors.add(Author("Author$i"))
        }
        return authors
    }

}