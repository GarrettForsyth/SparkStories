package com.example.android.writeitsayithearit.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.android.writeitsayithearit.model.story.Story

@Dao
abstract class StoryDao {

    @Insert
    abstract fun insert(story: Story)

    @Insert
    abstract fun insert(stories: List<Story>)

    @Update
    abstract fun update(story: Story)

    @Query("SELECT * from stories WHERE id = :id")
    abstract fun story(id: Int): LiveData<Story>

    @RawQuery(observedEntities = [ Story::class ])
    abstract fun stories(query: SupportSQLiteQuery): DataSource.Factory<Int, Story>

}
