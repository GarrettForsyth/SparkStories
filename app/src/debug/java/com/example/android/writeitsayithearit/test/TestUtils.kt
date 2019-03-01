package com.example.android.writeitsayithearit.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.test.core.app.ApplicationProvider
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.model.author.Author
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import java.util.*

/**
 * Some predefined data and helper functions for testing.
 */
object TestUtils {

    private val now = Calendar.getInstance().timeInMillis
    private val twoDays = 172800000

    val STARTING_AUTHORS = listOf(
        Author( "Bob"),
        Author("Anna"),
        Author("Joe"),
        Author("Eve")
    )
    val STARTING_CUES = listOf(
        Cue(
            "You find a rock, and realize anyone holding it must tell the truth.",
            STARTING_AUTHORS[0].name,
            now - 9 - twoDays,
            90
        ),
        Cue(
            "One day you look up at the sky and notice the moon is gone.",
            STARTING_AUTHORS[0].name,
            now - 100 - twoDays,
            3
        ),
        Cue(
            "A new submarine design reaches depths never before explored.",
            STARTING_AUTHORS[0].name,
            now - 7,
            100
        ),
        Cue(
            "A mad scientist threatens the world with his death ray.",
            STARTING_AUTHORS[1].name,
            now - 50 - twoDays,
            5
        ),
        Cue(
            "The largest hurricane in history is approaching.",
            STARTING_AUTHORS[1].name,
            now - 5,
            4
        ),
        Cue(
            "The allies lost the first world war.",
            STARTING_AUTHORS[1].name,
            now - 25 - twoDays,
            20
        ),
        Cue(
            "Dogs suddenly gain the ability to speak.",
            STARTING_AUTHORS[2].name,
            now - 3,
            16
        ),
        Cue(
            "A man drives his motorcycle across the country.",
            STARTING_AUTHORS[3].name,
            now - 2,
            12
        ),
        Cue(
            "A criminal tries to reintegrate with society.",
            STARTING_AUTHORS[3].name,
            now - 1,
            10
        ),
        Cue(
            "A writer solos a night on the town for inspiration.",
            STARTING_AUTHORS[3].name,
            now,
            200
        )
    )


    val STARTING_STORIES = listOf(
        Story(
            "This is the tale about: You find a rock, and realize anyone holding it must tell the truth.",
            STARTING_AUTHORS[0].name,
            1,
            now - 9 - twoDays,
            90
        ),
        Story(
            "This is the tale about: One day you look up at the sky and notice the moon is gone.",
            STARTING_AUTHORS[0].name,
            2,
            now - 100 - twoDays,
            3
        ),
        Story(
            "This is the tale about: A new submarine design reaches depths never before explored.",
            STARTING_AUTHORS[0].name,
            3,
            now - 7,
            100
        ),
        Story(
            "This is the tale about: A mad scientist threatens the world with his death ray.",
            STARTING_AUTHORS[1].name,
            4,
            now - 50 - twoDays,
            5
        ),
        Story(
            "This is the tale about: The largest hurricane in history is approaching.",
            STARTING_AUTHORS[1].name,
            5,
            now - 5,
            4
        ),
        Story(
            "This is the tale about: The allies lost the first world war.",
            STARTING_AUTHORS[1].name,
            6,
            now - 25 - twoDays,
            20
        ),
        Story(
            "This is the tale about: Dogs suddenly gain the ability to speak.",
            STARTING_AUTHORS[2].name,
            7,
            now - 3,
            16
        ),
        Story(
            "This is the tale about: A man drives his motorcycle across the country.",
            STARTING_AUTHORS[3].name,
            8,
            now - 2,
            12
        ),
        Story(
            "This is the tale about: A criminal tries to reintegrate with society.",
            STARTING_AUTHORS[3].name,
            10,
            now - 1,
            10
        ),
        Story(
            "This is the tale about: A writer solos a night on the town for inspiration.",
            STARTING_AUTHORS[3].name,
            10,
            now,
            200
        )
    )

    val SORT_NEW_INDICES = listOf(9,8,7,6,4,2,0,5,3,1)
    val SORT_TOP_INDICES = listOf(9,2,0,5,6,7,8,3,4,1)
    val SORT_HOT_INDICES = listOf(9,2,6,7,8,4)
    val FILTER_SORT_HOT_INDICES = listOf(9,6,7,8) // filter on 'to'
    val FILTER_SORT_NEW_INDICES = listOf(9,8,7,6) // filter on 'to'
    val FILTER_SORT_TOP_INDICES = listOf(9,6,7,8) // filter on 'to'
    val FILTER_AUTHOR_NEW_INDICES = listOf(2,0,1)
    val FILTER_STRING_TO = "to"

    fun createTestCue() = Cue(
        text = "Test cue text. Very interesting stuff.",
        author = "Test Cue Author",
        creationDate = 0,
        rating = 0
    )

    fun createTestStory() = Story(
        text = "This is a test story. It was the best of stories, it was the worst of stories.",
        author = "Test Story Author",
        cueId = 1,
        creationDate = 0,
        rating = 0
    )

    fun createTestAuthor() = Author(name = "Test Author")

}