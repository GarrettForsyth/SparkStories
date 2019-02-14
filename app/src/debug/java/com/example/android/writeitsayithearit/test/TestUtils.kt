package com.example.android.writeitsayithearit.test

import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story
import java.util.*

/**
 * Some predefined data and helper functions for testing.
 */
object TestUtils {

    private val now = Calendar.getInstance().timeInMillis
    private val twoDays = 172800000

    val listOfStartingCues = listOf(
            Cue( "You find a rock, and realize anyone holding it must tell the truth.",
                   now - 9 - twoDays,
                    90),
            Cue( "One day you look up at the sky and notice the moon is gone.",
                    now - 100 - twoDays,
                    3),
            Cue( "A new submarine design reaches depths never before explored.",
                    now - 7,
                    100),
            Cue( "A mad scientist threatens the world with his death ray.",
                   now - 50 - twoDays,
                    5),
            Cue( "The largest hurricane in history is approaching.",
                    now - 5,
                    4),
            Cue( "The allies lost the first world war.",
                   now - 25 - twoDays,
                    20),
            Cue( "Dogs suddenly gain the ability to speak.",
                    now - 3,
                    16),
            Cue( "A man drives his motorcycle across the country.",
                    now - 2,
                    12),
            Cue( "A criminal tries to reintegrate with society.",
                    now - 1,
                    10),
            Cue( "A writer solos a night on the town for inspiration.",
                    now,
                    200)
    )


    val listOfStartingStories = listOf(
            Story( "This is the tale about: You find a rock, and realize anyone holding it must tell the truth.",
                    1,
                    now - 9 - twoDays,
                    90),
            Story( "This is the tale about: One day you look up at the sky and notice the moon is gone.",
                    2,
                    now - 100 - twoDays,
                    3),
            Story( "This is the tale about: A new submarine design reaches depths never before explored.",
                    3,
                    now - 7,
                    100),
            Story( "This is the tale about: A mad scientist threatens the world with his death ray.",
                    4,
                    now - 50 - twoDays,
                    5),
            Story( "This is the tale about: The largest hurricane in history is approaching.",
                    5,
                    now - 5,
                    4),
            Story( "This is the tale about: The allies lost the first world war.",
                    6,
                    now - 25 - twoDays,
                    20),
            Story( "This is the tale about: Dogs suddenly gain the ability to speak.",
                    7,
                    now - 3,
                    16),
            Story( "This is the tale about: A man drives his motorcycle across the country.",
                    8,
                    now - 2,
                    12),
            Story( "This is the tale about: A criminal tries to reintegrate with society.",
                    10,
                    now - 1,
                    10),
            Story( "This is the tale about: A writer solos a night on the town for inspiration.",
                    1,
                    now,
                    200)
    )

    val sortByNewIndices = listOf(9,8,7,6,4,2,0,5,3,1)
    val sortByTopIndices = listOf(9,2,0,5,6,7,8,3,4,1)
    val sortByHotIndices = listOf(9,2,6,7,8,4)
    val sortByHotAndFilter = listOf(9,6,7,8) // filter on 'to'

    fun createTestCue(): Cue = Cue( "this is a test cue", now, 0)

    fun createTestStory(): Story = Story( "this is a test story. sure is great", 0, now, 0)
}