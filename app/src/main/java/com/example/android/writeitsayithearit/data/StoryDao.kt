package com.example.android.writeitsayithearit.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story

@Dao
abstract class StoryDao {

    @Insert
    abstract fun insert(story: Story)

    @Insert
    abstract fun insert(stories: List<Story>)

    @Query("SELECT * from story WHERE id = :id")
    abstract fun story(id: Int): LiveData<Story>

    @Query("SELECT * from story")
    abstract fun stories(): LiveData<List<Story>>

}
