package com.example.android.sparkstories.ui.splash


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.android.sparkstories.R
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.databinding.FragmentNewScreenNameBinding
import com.example.android.sparkstories.databinding.FragmentSplashBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.signup.NewScreenNameFragmentDirections
import com.example.android.sparkstories.ui.signup.NewScreenNameViewModel
import com.firebase.ui.auth.IdpResponse
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@OpenForTesting
class SplashFragment : Fragment(), Injectable {

    @Inject
    lateinit var authenticator: Authenticator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var splashViewModel: SplashViewModel

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        )

        splashViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SplashViewModel::class.java)

        binding.viewmodel = splashViewModel
        binding.isLoading = false

        Timber.d("test before observing..")


        if (authenticator.isNotAuthenticated()) {
            Timber.d("mytest User is not authenticated.")
            authenticator.authenticateUser(this)
        } else {
            Timber.d("mytest User is authenticated.. checking user has sceen name")
            observeDoesUserHaveScreenName()
        }

        return binding.root
    }

    private fun observeDoesUserHaveScreenName() {
        splashViewModel.doesUserHaveScreenName.observe(this, Observer { result ->
            when (result?.status) {
                Status.SUCCESS -> {
                    Timber.d("screenName test: successful result")
                    result.data?.let {
                        if (result.data) {
                            Timber.d("screenName test: true --> user has screen name")
                            navController().navigate(
                                SplashFragmentDirections.actionSplashFragmentToCuesFragment()
                            )
                        } else { // user exists but does not have screen name
                            Timber.d("screenName test: false --> user does not have screen name")
                            navController().navigate(
                                SplashFragmentDirections.actionSplashFragmentToCreateScreenNameFragment()
                            )
                        } // user does not exist
                        Timber.e("Trying to check screen name of a user that doesn't exist..")
                    }
                    binding.isLoading = false
                }
                Status.LOADING -> {
                    Timber.d("screenName test: loading..")
                    binding.isLoading = true
                }
                Status.ERROR -> {
                    Timber.d("screenName test: error response: ${result.message}")
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    binding.isLoading = false
                    // TODO: If an error occurs, then we can't let the user proceed or create a new user name.
                    // Instead, retry (!?) and if errors continue, show a dialog indicated that a connection
                    // to the server could not be made to authenticate identity (actually to check if the user
                    // already has a screename) and exit the app
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.d("test kickoff result received!")
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                Timber.d("test checking if user has screen name..")
                observeDoesUserHaveScreenName()

            } else {
                // TODO: Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Timber.e("Log in failed: ${response?.getError()}")
            }
        }
    }

    fun navController() = findNavController()

    companion object {
        const val RC_SIGN_IN = 0
    }
}
