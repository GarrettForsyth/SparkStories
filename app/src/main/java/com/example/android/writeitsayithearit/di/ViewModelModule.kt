package com.example.android.writeitsayithearit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.writeitsayithearit.ui.comments.CommentsViewModel
import com.example.android.writeitsayithearit.ui.comments.NewCommentViewModel
import com.example.android.writeitsayithearit.ui.cues.CueViewModel
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.ui.stories.StoryViewModel
import com.example.android.writeitsayithearit.viewmodel.WriteItSayItHearItViewModelFactory
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
    abstract fun bindViewModelFactory(factory: WriteItSayItHearItViewModelFactory) : ViewModelProvider.Factory

}