package com.example.android.sparkstories.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.author.AuthorRepository
import com.example.android.sparkstories.ui.signup.NewScreenNameViewModel
import com.example.android.sparkstories.ui.splash.SplashViewModel
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
@Ignore
class SplashViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val authorRepository: AuthorRepository = mockk(relaxed = true)

    lateinit var splashViewModel: SplashViewModel

    @Before
    fun init() {
        splashViewModel = SplashViewModel(authorRepository)
        mockObserverFor(splashViewModel.doesUserHaveScreenName)
    }

//    @Test
//    fun doesUserHaveScreenName_shouldUpdateWhenAuthorRepositoryUpdates() {
//        val doesUserHaveScreenNameResponse = MutableLiveData<Resource<Boolean>>()
//        every { authorRepository.checkUserHasScreenName() } returns doesUserHaveScreenNameResponse
//
//        authorRepository.checkUserHasScreenName()
//        doesUserHaveScreenNameResponse.value = Resource.success(true)
//        assertEquals(splashViewModel.doesUserHaveScreenName.value, doesUserHaveScreenNameResponse.value)
//    }

}

