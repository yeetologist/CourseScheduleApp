package com.dicoding.courseschedule.ui.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var dailyReminder: DailyReminder

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission rejected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))

        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val nightMode = when (newValue) {
                "on" -> AppCompatDelegate.MODE_NIGHT_YES
                "auto" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }

            updateTheme(nightMode)
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        dailyReminder = DailyReminder()
        val notificationSwitch =
            findPreference<SwitchPreference>(getString(R.string.pref_key_notify))

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        notificationSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val enableNotifications = newValue as Boolean


            if (enableNotifications) {
                // Schedule notifications using your DailyReminder logic
                dailyReminder.setDailyReminder(requireContext())

            } else {
                // Cancel notifications
                dailyReminder.cancelAlarm(requireContext())
            }

            true
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}