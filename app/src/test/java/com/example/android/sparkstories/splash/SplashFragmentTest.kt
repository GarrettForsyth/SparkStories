package com.example.android.sparkstories.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.TestApp
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.ui.signup.NewScreenNameFragment
import com.example.android.sparkstories.ui.signup.NewScreenNameFragmentDirections
import com.example.android.sparkstories.ui.splash.SplashFragment
import com.example.android.sparkstories.ui.splash.SplashFragmentDirections
import com.example.android.sparkstories.ui.util.events.Event
import com.example.android.sparkstories.util.ViewModelUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class SplashFragmentTest {

    private val scenario = launchInContainer(
        TestSplashFragment::class.java,
        null,
        TestSplashFragmentFactory()
    )

    @Test
    fun onUserHasScreenName_NavigateToCuesFragment() {
        scenario.onFragment {
            it.doesUserHaveScreenName.value = Resource.success(true)
            verify(exactly = 1) {
                it.navController.navigate(
                    SplashFragmentDirections.actionSplashFragmentToCuesFragment()
                )
            }
        }
    }

    @Test
    fun onUserDoesNotHaveScreenName_NavigateToNewScreenNameFragment() {
        scenario.onFragment {
            it.doesUserHaveScreenName.value = Resource.success(false)
            verify(exactly = 1) {
                it.navController.navigate(
                    SplashFragmentDirections.actionSplashFragmentToCreateScreenNameFragment()
                )
            }
        }
    }

    /**
     * A factory that returns a CuesFragment with mocked dependencies.
     *
     * This allows the dependencies to be mocked BEFORE the fragment
     * is attached using FragmentScenario.launch.
     */
    class TestSplashFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as TestSplashFragment).apply {
                this.splashViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.splashViewModel)
                this.authenticator = mockk(relaxed = true)
                every { splashViewModel.doesUserHaveScreenName } returns doesUserHaveScreenName
            }
        }
    }

    /**
     * Simply overrides the nav controller to verify correct actions are
     * being called when expected.
     */
    class TestSplashFragment : SplashFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController
        val doesUserHaveScreenName = MutableLiveData<Resource<Boolean>>()
    }
}

