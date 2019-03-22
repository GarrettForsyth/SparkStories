package com.example.android.sparkstories.util

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import org.junit.runner.Description
import timber.log.Timber
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * A Junit rule that registers Architecture Components' background threads as an Espresso idling
 * resource.
 */
class TaskExecutorWithIdlingResourceRule : CountingTaskExecutorRule() {
    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()
    private val idlingResource: IdlingResource = object : IdlingResource {
        override fun getName(): String {
            return "architecture components idling resource $id"
        }

        override fun isIdleNow(): Boolean {
            val isIdle = this@TaskExecutorWithIdlingResourceRule.isIdle
//            if (isIdle) Timber.d("TaskExecutorIdlingResource idle check: true mytrace")
//            else Timber.d("TaskExecutorIdlingResource idle check: false mytrace")
            return isIdle
        }

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
            callbacks.add(callback)
        }
    }
    private val callbacks = CopyOnWriteArrayList<IdlingResource.ResourceCallback>()
    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }

    override fun finished(description: Description?) {
        drainTasks(10, TimeUnit.SECONDS)
        callbacks.clear()
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun onIdle() {
        super.onIdle()
        for (callback in callbacks) {
            Timber.d("TaskExecutorIdlingResource transitionToIdle mytrace")
            callback.onTransitionToIdle()
        }
    }

}