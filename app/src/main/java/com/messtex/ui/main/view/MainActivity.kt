package com.messtex.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.messtex.R
import com.messtex.ui.fragments.view.StepMeterInfo
import com.messtex.ui.fragments.view.StepUserInfo
import com.messtex.ui.main.adapter.MainActivityAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainActivityAdapter(supportFragmentManager)
        adapter.addFragment(StepUserInfo(), "UserInfo")
        adapter.addFragment(StepMeterInfo(), "MeterInfo")
        viewPager.adapter = adapter
    }
}