package com.example.android.sparkstories.ui.splash

import androidx.lifecycle.*
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.author.AuthorRepository
import com.example.android.sparkstories.test.asLiveData
import com.example.android.sparkstories.ui.util.events.Event
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authorRepository: AuthorRepository
) : ViewModel() {

    val doesUserHaveScreenName: LiveData<Resource<Boolean>> by lazy {
        Transformations.switchMap(authorRepository.checkUserHasScreenName()) {
            it.asLiveData()
        }
    }

}
