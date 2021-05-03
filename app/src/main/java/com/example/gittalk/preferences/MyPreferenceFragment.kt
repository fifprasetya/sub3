package com.example.gittalk.preferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.gittalk.R
import com.example.gittalk.receiver.AlarmReceiver

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMAINDER: String

    private lateinit var isRemainderPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
        alarmReceiver = AlarmReceiver()
        init()
        setSummaries()
    }

    private fun init() {
        REMAINDER = resources.getString(R.string.key_remainder)
        isRemainderPreference = findPreference<SwitchPreference>(REMAINDER) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isRemainderPreference.isChecked = sh.getBoolean(REMAINDER, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMAINDER) {
            isRemainderPreference.isChecked = sharedPreferences.getBoolean(REMAINDER, false)
            if (isRemainderPreference.isChecked) {
                alarmReceiver.setRepeatingAlarm(activity,"Repeating alarm set up", "09:00" ,"Pengingat Github")
            } else
                alarmReceiver.cancelAlarm(activity)

        }
    }


}