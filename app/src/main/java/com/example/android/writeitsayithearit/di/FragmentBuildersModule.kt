package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.ui.comments.CommentsFragment
import com.example.android.writeitsayithearit.ui.cues.CueFragment
import com.example.android.writeitsayithearit.ui.cues.CuesFragment
import com.example.android.writeitsayithearit.ui.cues.NewCueFragment
import com.example.android.writeitsayithearit.ui.narrations.NarrationsFragment
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragment
import com.example.android.writeitsayithearit.ui.stories.StoriesFragment
import com.example.android.writeitsayithearit.ui.stories.StoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCuesFragment() : CuesFragment

    @ContributesAndroidInjector
    abstract fun contributeCueFragment() : CueFragment

    @ContributesAndroidInjector
    abstract fun contributeNewCueFragment() : NewCueFragment

    @ContributesAndroidInjector
    abstract fun contributeStoriesFragment() : StoriesFragment

    @ContributesAndroidInjector
    abstract fun contributeNewStoryFragment() : NewStoryFragment

    @ContributesAndroidInjector
    abstract fun contributeStoryFragment() : StoryFragment

    @ContributesAndroidInjector
    abstract fun contributeNarrationsFragment() : NarrationsFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentsFragment() : CommentsFragment

}