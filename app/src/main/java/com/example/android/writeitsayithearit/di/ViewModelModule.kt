package com.example.android.writeitsayithearit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.writeitsayithearit.ui.CuesViewModel
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
    abstract fun bindViewModelFactory(factory: WriteItSayItHearItViewModelFactory) : ViewModelProvider.Factory

}