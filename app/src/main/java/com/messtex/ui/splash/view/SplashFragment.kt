package com.messtex.ui.splash.view


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.messtex.R
import com.messtex.ui.main.view.MainActivity

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            if (onboardingFinished()){
                findNavController().navigate(R.id.action_splashFragment_to_home2)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }

        }, 3000)

        return inflater.inflate(R.layout.activity_splash, container, false)
    }

    private fun onboardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}