package com.example.android.sparkstories.ui.signup

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.author.ScreenNameTextField
import com.example.android.sparkstories.repos.AuthorRepository
import com.example.android.sparkstories.ui.util.TextWatcherAdapter
import com.example.android.sparkstories.ui.util.events.Event
import javax.inject.Inject

class NewScreenNameViewModel @Inject constructor(
    private val authorRepository: AuthorRepository
): ViewModel() {

    val doesUserExist = authorRepository.checkUserExists()

    var screenName = ScreenNameTextField()

    private val _screenNameFieldErrorMessage = MutableLiveData<String>()
    val screenNameFieldErrorMessage:  LiveData<String>
        get() = _screenNameFieldErrorMessage

    private val checkScreenNameAvailable = MutableLiveData<String>()
    private val _isScreenNameAvailableResponse = Transformations.switchMap(checkScreenNameAvailable) {screenName ->
        authorRepository.checkScreenNameAvailable(screenName)
    }
    val isScreenNameAvailableResponse:  LiveData<Resource<Pair<Boolean, String>>> = _isScreenNameAvailableResponse

    private val submitScreenName = MutableLiveData<Boolean>()
    private val _submitScreenNameResponse = Transformations.switchMap(submitScreenName) { authorRepository.submitScreenName(screenName.text)}
    val submitScreenNameResponse: LiveData<Resource<Unit>> = _submitScreenNameResponse

    private val _invalidScreenNameSnackBar = MutableLiveData<Event<Boolean>>()
    val invalidScreenNameSnackBar: LiveData<Event<Boolean>>
        get() = _invalidScreenNameSnackBar

    private val _shouldNavigateToCues = MutableLiveData<Event<Boolean>>()
    val shouldNavigateToCues: LiveData<Event<Boolean>>
        get() = _shouldNavigateToCues

    fun onSubmitScreenName() {
        if(screenName.isValid() && _isScreenNameAvailableResponse.value?.data?.first!!) {
            submitScreenName.postValue(true)
        }else {
            _invalidScreenNameSnackBar.value = Event(true)
        }
    }

    fun screenNameTextChangeListener(): TextWatcher {
        return object : TextWatcherAdapter() {
            override fun afterTextChanged(text: Editable?) {
                synchronized(screenName) { // make sure checks are operating on the same screen name
                    _screenNameFieldErrorMessage.postValue(screenName.getErrorMessages())

                    // Only send network request to check if client side validations pass
                    if(screenName.getErrorMessages().isBlank()) {
                        checkScreenNameAvailable.postValue(screenName.text)
                    }
                }
            }
        }
    }
}