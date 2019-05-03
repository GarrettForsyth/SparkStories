package com.example.android.sparkstories.di

import com.example.android.sparkstories.ui.comments.CommentsFragment
import com.example.android.sparkstories.ui.comments.NewCommentFragment
import com.example.android.sparkstories.ui.cues.CueFragment
import com.example.android.sparkstories.ui.cues.CuesFragment
import com.example.android.sparkstories.ui.cues.NewCueFragment
import com.example.android.sparkstories.ui.narrations.NarrationsFragment
import com.example.android.sparkstories.ui.signup.NewScreenNameFragment
import com.example.android.sparkstories.ui.splash.SplashFragment
import com.example.android.sparkstories.ui.stories.NewStoryFragment
import com.example.android.sparkstories.ui.stories.StoriesFragment
import com.example.android.sparkstories.ui.stories.StoryFragment
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

    @ContributesAndroidInjector
    abstract fun contributeNewCommentFragment() : NewCommentFragment

    @ContributesAndroidInjector
    abstract fun contributeNewScreenNameFragment() : NewScreenNameFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment() : SplashFragment

}