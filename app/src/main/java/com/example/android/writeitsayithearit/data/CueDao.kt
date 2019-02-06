package com.example.android.writeitsayithearit.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.writeitsayithearit.vo.Cue

@Dao
abstract class CueDao {

    @Insert
    abstract fun insert(cues: Cue)

    @Insert
    abstract fun insert(cues: List<Cue>)

    @Query("SELECT * from cue WHERE id = :id")
    abstract fun cue(id: Int): LiveData<Cue>

    @Query("SELECT * from cue")
    abstract fun cues(): LiveData<List<Cue>>

}