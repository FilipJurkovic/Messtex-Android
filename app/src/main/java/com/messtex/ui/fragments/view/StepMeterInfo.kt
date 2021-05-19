package com.messtex.ui.fragments.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.messtex.R
import com.messtex.ui.fragments.viewmodel.StepMeterInfoViewModel
import kotlinx.android.synthetic.main.step_meter_info_fragment.*

class StepMeterInfo : Fragment() {

    companion object {
        fun newInstance() = StepMeterInfo()
    }

    private lateinit var stepMeterInfoViewModel: StepMeterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_meter_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stepMeterInfoViewModel = ViewModelProvider(this).get(StepMeterInfoViewModel::class.java)
        stepMeterInfoViewModel.meterData.observe(this, Observer{
            meterReadingText.setText(it.meterValue.toString())
        })
        meterTypeList.setOnItemClickListener({
        })
    })

}