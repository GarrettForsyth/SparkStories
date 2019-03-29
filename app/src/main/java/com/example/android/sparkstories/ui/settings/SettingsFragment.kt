package com.example.android.sparkstories.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.android.sparkstories.R
import com.firebase.ui.auth.AuthUI

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(getString(R.string.log_out_key))
            ?.setOnPreferenceClickListener { it ->
                AuthUI.getInstance().signOut(context!!)
                true
            }

        findPreference<Preference>(getString(R.string.delete_account_key))
            ?.setOnPreferenceClickListener { it ->
                AuthUI.getInstance().delete(context!!)
                true
            }
    }
}