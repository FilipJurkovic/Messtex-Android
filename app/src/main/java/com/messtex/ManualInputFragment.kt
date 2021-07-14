package com.messtex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.MeterData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_manual_input.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ManualInputFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_input, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         var meterType : String = ""

        var isCounterValueSet : Boolean = false

        if(!isCounterValueSet){
            nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main_disabled)
            nextButtonManualInput.isEnabled = false
        } else{
            nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main)
            nextButtonManualInput.isEnabled = true
        }

        Log.d("Meter index", sharedViewModel.meterIndex.toString())

        when (sharedViewModel.meterIndex) {
            0 -> {
                meterType = "Heat meter"
            }
            1 -> {
                meterType = "Heat meter allocator"
            }
            2 -> {
                meterType = "Water meter"
            }
        }

        counterTypeInput.setText(meterType)
        meterTypeText.text = meterType

        val standardFormat = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date : Date = standardFormat.parse(Date().toString())
        readingDateInput.setText(formatter.format(date))

        counterValue.setText(sharedViewModel.meterValue.value?.replace(".", ""))
        nextButtonManualInput.setOnClickListener() {
            if (counterNumberInput.text != null && counterTypeInput.text != null && readingDateInput.text != null && counterValue.text != null){

            }

            val counterValueFormatted : String = counterValue.toString().substring(0, counterValue.toString().length-2)+"."+counterValue.toString().substring(counterValue.toString().length-2, counterValue.toString().length)
            sharedViewModel.meterData.add(MeterData(counterNumberInput.text.toString(), meterType, sharedViewModel.meterValue.value!! , reportMessageInput.text.toString()))

            Log.d("Counter value", counterValueFormatted)

            when (sharedViewModel.meterIndex) {
                0 -> {
                    sharedViewModel.heat_meter_step = true
                }
                1 -> {
                    sharedViewModel.heat_allocator_step = true
                }
                2 -> {
                    sharedViewModel.water_meter_step = true
                }
            }

            findNavController().navigate(R.id.action_manualInputFragment_to_readingStepsFragment)
        }
    }

}