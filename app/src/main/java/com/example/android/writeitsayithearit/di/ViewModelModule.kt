package com.example.android.writeitsayithearit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.writeitsayithearit.ui.cues.CuesViewModel
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.viewmodel.WriteItSayItHearItViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

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
    abstract fun bindViewModelFactory(factory: WriteItSayItHearItViewModelFactory) : ViewModelProvider.Factory

}