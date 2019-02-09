package com.example.android.writeitsayithearit.vo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Story (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull val text: String,
    val cueId: Int?
)