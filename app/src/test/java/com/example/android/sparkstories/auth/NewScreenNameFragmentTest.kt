package com.example.android.sparkstories.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.R
import com.example.android.sparkstories.TestApp
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.ui.signup.NewScreenNameFragment
import com.example.android.sparkstories.ui.signup.NewScreenNameFragmentDirections
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.util.ViewModelUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_new_screen_name.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class NewScreenNameFragmentTest {

    private val scenario = launchInContainer(
        TestNewScreenNameFragment::class.java,
        null,
        TestNewScreenNameFragmentFactory()
    )

    @Test
    fun clickSubmit() {
        scenario.onFragment {
            it.button_submit_screen_name.performClick()
            verify(exactly = 1) { it.newScreenNameViewModel.onSubmitScreenName() }
        }
    }

    @Test
    fun onUnSubmitScreenName_DoNotNavigateToCuesFragment() {
        scenario.onFragment {
            it.submitScreenNameResponse.value = Resource.error("", null)
            verify(exactly = 0) {
                it.navController.navigate(
                    NewScreenNameFragmentDirections
                        .actionCreateScreenNameFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun onErrorSubmitScreenName_NavigateToCuesFragment() {
        scenario.onFragment {
            it.submitScreenNameResponse.value = Resource.error("",null)
            verify(exactly = 0) {
                it.navController.navigate(
                    NewScreenNameFragmentDirections
                        .actionCreateScreenNameFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun navigateToCuesFragment() {
        scenario.onFragment {
            it.shouldNavigateToCues.value = Event(true)
            verify {
                it.navController.navigate(
                    NewScreenNameFragmentDirections
                        .actionCreateScreenNameFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun onUserExists_NavigateToCuesFragment() {
        scenario.onFragment {
            it.doesUserExist.value = Resource.success(true)
            verify(exactly = 1) {
                it.navController.navigate(
                    NewScreenNameFragmentDirections
                        .actionCreateScreenNameFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun onUserDoesNotExist_DoNotNavigateToCuesFragment() {
        scenario.onFragment {
            it.doesUserExist.value = Resource.success(false)
            verify(exactly = 0) {
                it.navController.navigate(
                    NewScreenNameFragmentDirections
                        .actionCreateScreenNameFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun onScreenNameFieldErrorMessage() {
        scenario.onFragment {
            val errorMessage = "This is an error message."
            it.screenNameFieldErrorMessage.postValue(errorMessage)
            assertTrue(it.text_input_layout.error.toString() == errorMessage)
        }
    }

    @Test
    fun showUnavailableMessage() {
        scenario.onFragment {
            val screenName = "taken_screen_name"
            every { it.newScreenNameViewModel.screenName.text } returns screenName
            val availabilityMessage = it.getString(R.string.name_unavailable)
            it.isScreenNameAvailableResponse.postValue(Resource.success(Pair(false, screenName)))
            assertEquals(it.text_input_layout.error.toString(), availabilityMessage)
        }
    }

    @Test
    fun noErrorIfUserNameAvailable() {
        scenario.onFragment {
            val screenName = "taken_screen_name"
            every { it.newScreenNameViewModel.screenName.text } returns screenName
            it.isScreenNameAvailableResponse.postValue(Resource.success(Pair(true, screenName)))
            assertNull(it.text_input_layout.error)
        }
    }

    @Test
    fun noErrorIfUserNameAvailableButUserHasChangedTextSinceRequest() {
        scenario.onFragment {
            every { it.newScreenNameViewModel.screenName.text } returns "abcdef"
            it.isScreenNameAvailableResponse.postValue(Resource.success(Pair(true, "abcde")))
            assertNull(it.text_input_layout.error)
        }
    }

    @Test
    fun noErrorIfUserNameUnAvailableButUserHasChangedTextSinceRequest() {
        scenario.onFragment {
            every { it.newScreenNameViewModel.screenName.text } returns "abcdef"
            it.isScreenNameAvailableResponse.postValue(Resource.success(Pair(false, "abcde")))
            assertNull(it.text_input_layout.error)
        }
    }

    @Test
    fun showInvalidScreenNameSnackbar() {
        val errorMessages = "Screen name cannot be blank!"
        scenario.onFragment {
            it.invalidScreenNameSnackBar.value = Event(true)
        }
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(errorMessages)
            )
        ).check(matches(isDisplayed()))
    }

    /**
     * A factory that returns a CuesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     */
    class TestNewScreenNameFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as TestNewScreenNameFragment).apply {
                this.newScreenNameViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.newScreenNameViewModel)

                every { newScreenNameViewModel.shouldNavigateToCues } returns shouldNavigateToCues
                every { newScreenNameViewModel.doesUserExist } returns doesUserExist
                every { newScreenNameViewModel.submitScreenNameResponse } returns submitScreenNameResponse
                every { newScreenNameViewModel.screenNameFieldErrorMessage } returns screenNameFieldErrorMessage
                every { newScreenNameViewModel.isScreenNameAvailableResponse } returns isScreenNameAvailableResponse
                every { newScreenNameViewModel.invalidScreenNameSnackBar } returns invalidScreenNameSnackBar
            }
        }
    }

    /**
     * Simply overrides the nav controller to verify correct actions are
     * being called when expected.
     */
    class TestNewScreenNameFragment : NewScreenNameFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
        val shouldNavigateToCues = MutableLiveData<Event<Boolean>>()
        val doesUserExist = MutableLiveData<Resource<Boolean>>()
        val submitScreenNameResponse = MutableLiveData<Resource<Unit>>()
        val screenNameFieldErrorMessage = MutableLiveData<String>()
        val isScreenNameAvailableResponse = MutableLiveData<Resource<Pair<Boolean,String>>>()
        val invalidScreenNameSnackBar = MutableLiveData<Event<Boolean>>()
    }

}
