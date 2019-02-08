package com.example.android.writeitsayithearit.util

import com.example.android.writeitsayithearit.AppExecutors
import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}
