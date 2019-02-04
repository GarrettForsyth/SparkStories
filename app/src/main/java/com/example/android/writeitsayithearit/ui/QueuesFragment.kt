package com.example.android.writeitsayithearit.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.di.Injectable
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 * Note: can use field injections after onStart
 */
class QueuesFragment : Fragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_queues, container, false)
    }

}
