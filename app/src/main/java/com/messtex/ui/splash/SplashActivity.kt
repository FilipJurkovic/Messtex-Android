package com.messtex.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.messtex.R
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.onboarding.OnboardingActivity
import java.util.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (onboardingFinished()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Log.d("Onboarding", onboardingFinished().toString())
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }, 3000)
    }

    private fun onboardingFinished(): Boolean{
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}