package com.example.android.sparkstories.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.sparkstories.model.author.Author

@Dao
abstract class AuthorDao {

    @Insert
    abstract fun insert(author: Author)

    @Insert
    abstract fun insert(authors: List<Author>)

    @Query("SELECT * from authors WHERE name = :name")
    abstract fun author(name: String): LiveData<Author>

    @Query("SELECT * from authors")
    abstract fun allAuthors(): LiveData<List<Author>>

}