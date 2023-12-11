package com.dicoding.courseschedule.ui.setting

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val DarkMode: ListPreference? = findPreference(getString(R.string.pref_key_dark))

        DarkMode?.setOnPreferenceChangeListener { _, newValue ->
            val stringValue = newValue.toString()
            when (stringValue) {
                getString(R.string.pref_dark_auto) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        updateTheme(NightMode.AUTO.value)
                    } else {
                        updateTheme(NightMode.ON.value)
                    }
                }
                getString(R.string.pref_dark_off) -> {
                    updateTheme(NightMode.OFF.value)
                }
                else -> {
                    updateTheme(NightMode.ON.value)
                }
            }

            true
        }




        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        val switchNotification: SwitchPreference? = findPreference(getString(R.string.pref_key_notify))

        switchNotification?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as? Boolean ?: false
            val broadcast = DailyReminder()

            if (isEnabled)  {
                broadcast.setDailyReminder(requireContext())
                showToast("Enabled")
            } else {
                broadcast.cancelAlarm(requireContext())
                showToast("Disabled")
            }

            true
        }


    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}