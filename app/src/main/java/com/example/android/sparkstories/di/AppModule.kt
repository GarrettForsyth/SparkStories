package com.example.android.sparkstories.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.api.WriteItSayItHearItService
import com.example.android.sparkstories.data.local.*
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.repos.cue.CueBoundaryCallback
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.utils.SparkStoriesQueryHelper
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application, dbSeed: DatabaseSeed): SharedPreferences {
        //TODO: Log in as the first starting author (temporary for tests)
        val fileName = " com.example.android.sparkstories"
        val pref = application.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return pref
    }

    @Singleton
    @Provides
    fun provideService() : WriteItSayItHearItService {
        return WriteItSayItHearItService()
    }

    @Singleton
    @Provides
    fun provideCueDao(db: WriteItSayItHearItDatabase): CueDao {
        return db.cueDao()
    }

    @Singleton
    @Provides
    fun provideStoryDao(db: WriteItSayItHearItDatabase): StoryDao {
        return db.storyDao()
    }

    @Singleton
    @Provides
    fun provideAuthorDao(db: WriteItSayItHearItDatabase): AuthorDao {
        return db.authorDao()
    }

    @Singleton
    @Provides
    fun provideCommentDao(db: WriteItSayItHearItDatabase): CommentDao {
        return db.commentDao()
    }

    @Singleton
    @Provides
    fun provideSparkStoriesQueryHelper(): SparkStoriesQueryHelper {
        return SparkStoriesQueryHelper
    }

}