package com.example.android.writeitsayithearit.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.*
import com.example.android.writeitsayithearit.repos.utils.WSHQueryHelper
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application, dbSeed: DatabaseSeed): SharedPreferences {
        //TODO: Log in as the first starting author (temporary for tests)
        val fileName = " com.example.android.writeitsayithearit"
        val pref = application.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        pref.edit().apply {
            putString(PREFERENCE_AUTHOR, dbSeed.SEED_AUTHORS.first().name)
            apply()
        }
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
    fun provideWSHQueryHelper(): WSHQueryHelper {
        return WSHQueryHelper
    }


}