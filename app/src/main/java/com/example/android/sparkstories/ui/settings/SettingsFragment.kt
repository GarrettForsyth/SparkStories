package com.example.android.sparkstories.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.android.sparkstories.R
import com.firebase.ui.auth.AuthUI

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(getString(R.string.log_out_key))
            ?.setOnPreferenceClickListener { it ->
                AuthUI.getInstance().signOut(context!!).addOnCompleteListener {
                    activity?.finish()
                }
                true
            }

        findPreference<Preference>(getString(R.string.change_screen_name_key))
            ?.setOnPreferenceClickListener { it ->
                navController().navigate(
                    SettingsFragmentDirections.actionSettingsFragmentToCreateScreenNameFragment()
                )
                true
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.mipmap.table_top_port)
    }

    fun navController() = findNavController()

}