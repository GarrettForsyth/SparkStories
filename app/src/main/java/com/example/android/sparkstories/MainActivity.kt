package com.example.android.sparkstories

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.ui.splash.SplashFragment
import com.example.android.sparkstories.ui.splash.SplashFragment.Companion.RC_SIGN_IN
import com.example.android.sparkstories.ui.splash.SplashFragmentDirections
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNav()

    }

    private fun setupBottomNav() {
        bottom_navigation.setupWithNavController(
            findNavController(
                this, R.id.nav_fragment
            )
        )
    }

}
