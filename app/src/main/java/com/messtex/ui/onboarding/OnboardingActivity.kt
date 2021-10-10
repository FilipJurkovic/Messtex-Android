package com.messtex.ui.onboarding

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.messtex.R
import java.util.*


class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this, getLocale())
        setContentView(R.layout.activity_onboarding)

    }

    private fun getLocale(): String? {
        val sharedPref = getSharedPreferences("locale", Context.MODE_PRIVATE)
        return sharedPref.getString("language", "de")
    }

    private fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}