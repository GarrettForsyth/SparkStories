package com.example.android.sparkstories.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.android.sparkstories.model.cue.Cue

@Dao
abstract class CueDao {

    @Insert
    abstract fun insert(cues: Cue)

    @Insert
    abstract fun insert(cues: List<Cue>)

    @Update
    abstract fun update(cue: Cue)

    @Query("SELECT * from cues WHERE id = :id")
    abstract fun cue(id: Int): LiveData<Cue>

    @RawQuery(observedEntities = [ Cue::class ])
    abstract fun cues(query: SupportSQLiteQuery): DataSource.Factory<Int, Cue>


}