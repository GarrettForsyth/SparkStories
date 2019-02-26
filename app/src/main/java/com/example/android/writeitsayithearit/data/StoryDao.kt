package com.example.android.writeitsayithearit.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.android.writeitsayithearit.model.story.Story

@Dao
abstract class StoryDao {

    @Insert
    abstract fun insert(story: Story)

    @Insert
    abstract fun insert(stories: List<Story>)

    @Query("SELECT * from stories WHERE id = :id")
    abstract fun story(id: Int): LiveData<Story>

    @RawQuery(observedEntities = [ Story::class ])
    abstract fun stories(query: SupportSQLiteQuery): LiveData<List<Story>>

}
