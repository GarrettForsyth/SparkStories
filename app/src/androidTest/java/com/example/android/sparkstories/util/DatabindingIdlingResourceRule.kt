package com.example.android.sparkstories.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * Registers an idling resource for all fragment views that use data binding.
 *
 * A reference to the current activity's bindings are required to watch when bindings are pending.
 * [ActivityTestScenario] will not run .onActivity { .. }  until an idle sync. This means it cannot
 * be called in the IdlingResources isIdleNow(). Instead, a reference to the activity is
 * is passed in, which has the potential for memory leaks. The activity's life cycle is therefore
 * observed to get rid of the activity reference when the activity dies.
 *
 * It also must wait for the [ActivityScenarioRule] to create its scenario. The [ActivityScenarioRule]
 * should be passed the [DataBindingIdlingResourceRule] in the test's @Before block.
 *
 */
class DataBindingIdlingResourceRule(
    activityScenarioRule: ActivityScenarioRule<out AppCompatActivity>
) : LifecycleObserver {

    var idlingResource: DataBindingIdlingResource? = null

    // Taking a reference to the activity like this has potential for memory leaks
    private var activity: AppCompatActivity? = null

    init {
        activityScenarioRule.scenario.onActivity { activity = it}
        activity!!.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerActivityViewBindings() {
        idlingResource = DataBindingIdlingResource(activity!!)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterActivityViewBindings() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        idlingResource = null
    }

}

