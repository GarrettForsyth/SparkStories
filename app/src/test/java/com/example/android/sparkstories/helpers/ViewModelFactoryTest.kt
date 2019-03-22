package com.example.android.sparkstories.helpers

import androidx.test.filters.SmallTest
import com.example.android.sparkstories.viewmodel.WriteItSayItHearItViewModelFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class ViewModelFactoryTest {

    @Test
    fun doesNotCrashOnEmptyMap() {
        WriteItSayItHearItViewModelFactory(mapOf())
    }

}