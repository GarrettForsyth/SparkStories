package com.example.android.sparkstories.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.android.sparkstories.model.story.Story

@Dao
abstract class StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(story: Story)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(stories: List<Story>)

    @Update
    abstract fun update(story: Story)

    @Query("SELECT * from stories WHERE id = :id")
    abstract fun story(id: String): LiveData<Story>

    @RawQuery(observedEntities = [ Story::class ])
    abstract fun stories(query: SupportSQLiteQuery): DataSource.Factory<Int, Story>

}
