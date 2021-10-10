package com.messtex.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.messtex.R
import com.messtex.data.models.CarbonData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.ui.onboarding.OnboardingActivity
import com.messtex.utils.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val repository = MainRepository()
        val factory = ViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                viewModel.faq.postValue(viewModel.getFaq())
                Log.d("FAQ", viewModel.faq.value.toString())
                viewModel.co2_data.postValue(CarbonData(viewModel.getCO2_Level() ?: 0.0))
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }

        Handler().postDelayed({
            if (onboardingFinished()) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("FAQ", viewModel.faq.value)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.d("Onboarding", onboardingFinished().toString())
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }, 3000)
    }

    private fun onboardingFinished(): Boolean {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}