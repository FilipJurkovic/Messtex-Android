package com.messtex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.MeterData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_manual_input.*
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
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

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        readingDateInput.setText(formatter.parse(Date().toString()).toString())


        nextButtonManualInput.setOnClickListener() {
            if (counterNumberInput.text != null && counterTypeInput.text != null && readingDateInput.text != null && counterValue.text != null){
                    sharedViewModel.meterData.add(MeterData(counterNumberInput.text.toString(), meterType, counterValue.toString(),reportMessageInput.text.toString()))
            }

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