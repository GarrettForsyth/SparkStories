package com.example.android.writeitsayithearit.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story

object TestUtils {


    val listOfStartingCues = listOf(
            Cue(1, "You find a rock, and realize anyone holding it must tell the truth."),
            Cue(2, "One day you look up at the sky and notice the moon is gone."),
            Cue(3, "A new submarine design reaches depths never before explored."),
            Cue(4, "A mad scientist threatens the world with his death ray."),
            Cue(5, "The largest hurricane in history is approaching."),
            Cue(6, "The allies lost the first world war."),
            Cue(7, "Dogs suddenly gain the ability to speak."),
            Cue(8, "A man drives his motorcycle across the country."),
            Cue(9, "A criminal tries to reintegrate with society."),
            Cue(10, "A writer solos a night on the town for inspiration.")
    )

    val listOfStartingStories = listOf(
            Story(1, "This is the story about: You find a rock, and realize anyone holding it must tell the truth.", 1),
            Story(2, "This is the story about: One day you look up at the sky and notice the moon is gone.", 2),
            Story(3, "This is the story about: A new submarine design reaches depths never before explored.", 3),
            Story(4, "This is the story about: A mad scientist threatens the world with his death ray.", 4),
            Story(5, "This is the story about: The largest hurricane in history is approaching.", 5),
            Story(6, "This is the story about: The allies lost the first world war.", 6),
            Story(7, "This is the story about: Dogs suddenly gain the ability to speak.", 7),
            Story(8, "This is the story about: A man drives his motorcycle across the country.", 8),
            Story(9, "This is the story about: A writer solos a night on the town for inspiration.", 10),
            Story(10, "This is the second story about: You find a rock, and realize anyone holding it must tell the truth.", 1),
            Story(11, "This is the third story about: You find a rock, and realize anyone holding it must tell the truth.", 1)
    )

    fun createTestCue(): Cue = Cue(0, "this is a test cue")

    fun createTestStory(): Story = Story(0, "this is a test story. sure is great", 0)
}