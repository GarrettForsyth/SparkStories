package com.example.android.sparkstories.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.sparkstories.ui.comments.CommentsViewModel
import com.example.android.sparkstories.ui.comments.NewCommentViewModel
import com.example.android.sparkstories.ui.cues.CueViewModel
import com.example.android.sparkstories.ui.cues.CuesViewModel
import com.example.android.sparkstories.ui.cues.NewCueViewModel
import com.example.android.sparkstories.ui.signup.NewScreenNameViewModel
import com.example.android.sparkstories.ui.stories.NewStoryViewModel
import com.example.android.sparkstories.ui.stories.StoriesViewModel
import com.example.android.sparkstories.ui.stories.StoryViewModel
import com.example.android.sparkstories.viewmodel.WriteItSayItHearItViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CueViewModel::class)
    abstract fun bindCueViewModel(cueViewModel: CueViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CuesViewModel::class)
    abstract fun bindCuesViewModel(cuesViewModel: CuesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewCueViewModel::class)
    abstract fun bindNewCueViewModel(newCueViewModel: NewCueViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewStoryViewModel::class)
    abstract fun bindNewStoryViewModel(newStoryViewModel: NewStoryViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StoriesViewModel::class)
    abstract fun bindStoriesViewModel(storiesViewModel: StoriesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StoryViewModel::class)
    abstract fun bindStoryViewModel(storyViewModel: StoryViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    abstract fun bindCommentsViewModel(commentsViewModel: CommentsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewCommentViewModel::class)
    abstract fun bindNewCommentViewModel(newCommentViewModel: NewCommentViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewScreenNameViewModel::class)
    abstract fun bindNewScreenNameViewModel(createNewScreenName: NewScreenNameViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: WriteItSayItHearItViewModelFactory) : ViewModelProvider.Factory

}