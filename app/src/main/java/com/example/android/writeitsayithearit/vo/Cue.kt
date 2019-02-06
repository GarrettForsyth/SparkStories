package com.example.android.writeitsayithearit.vo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cue(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @NonNull val text: String
)