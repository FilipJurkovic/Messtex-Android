package com.messtex

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.android.synthetic.main.fragment_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding.view.*


class OnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val fragmentList = arrayListOf<Fragment>(
            OnboardingStepOneFragment(),
            OnboardingStepTwoFragment(),
            OnboardingStepThreeFragment()
           // OnbardingPermissionFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        view.onboardingViewPager.adapter = adapter

        view.onboardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                view.pageIndicatorView.setSelected(position)
            }

        })


        view.onboardingButton.setOnClickListener {

            if (view.onboardingViewPager?.currentItem == 2){
                findNavController().navigate(R.id.action_onboardingFragment_to_home2)
                onboardingFinished()
            }else{
                view.onboardingViewPager?.currentItem = view.onboardingViewPager?.currentItem!! + 1
            }

            view.pageIndicatorView.setSelected(view.onboardingViewPager?.currentItem!!)
        }

        view.skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_home2)
            onboardingFinished()
        }
        return view
    }

    private fun onboardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }


}