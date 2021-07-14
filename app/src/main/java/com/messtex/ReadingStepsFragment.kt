package com.messtex

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.messtex.data.models.PostModelRecord
import com.messtex.data.models.UtilizationData
import com.messtex.data.models.ViewModelData
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_manual_input.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
import kotlinx.android.synthetic.main.fragment_reading_steps.nextButton

class ReadingStepsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val sharedViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_steps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
        heat_meter_step_success.isVisible = sharedViewModel.heat_meter_step
        heat_allcator_step_success.isVisible = sharedViewModel.heat_allocator_step
        water_meter_step_success.isVisible = sharedViewModel.water_meter_step
        contact_step_success.isVisible = sharedViewModel.contact_step

         if(!sharedViewModel.heat_meter_step  || !sharedViewModel.heat_allocator_step  || !sharedViewModel.water_meter_step  || !sharedViewModel.contact_step){
             nextButton.setBackgroundResource(R.drawable.background_button_main_disabled)
             nextButton.isEnabled = false
        } else{
             nextButton.setBackgroundResource(R.drawable.background_button_main)
             nextButton.isEnabled = true
        }

        if (sharedViewModel.heat_meter_step){
            heat_meter_button.setBackgroundResource(R.drawable.background_button_step_change)
            heat_meter_button.setTextColor(getResources().getColor(R.color.light))
        }

        if (sharedViewModel.water_meter_step){
            water_meter_button.setBackgroundResource(R.drawable.background_button_step_change)
            water_meter_button.setTextColor(getResources().getColor(R.color.light))
        }

        if (sharedViewModel.heat_allocator_step){
            heat_allocator_button.setBackgroundResource(R.drawable.background_button_step_change)
            heat_allocator_button.setTextColor(getResources().getColor(R.color.light))
        }

        if (sharedViewModel.contact_step){
            contact_button.setBackgroundResource(R.drawable.background_button_step_change)
            contact_button.setTextColor(getResources().getColor(R.color.light))
        }

        stepsBackButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        heat_meter_button.setOnClickListener() {
                sharedViewModel.meterIndex = 0
                if(sharedViewModel.isCameraAllowed){
                    startMeterReading(0)
                }else{
                    findNavController().navigate(R.id.action_readingStepsFragment_to_manualInputFragment)
                }
            }

        heat_allocator_button.setOnClickListener() {
            sharedViewModel.meterIndex = 1
            if(sharedViewModel.isCameraAllowed){
                startMeterReading(1)
            }else{
                findNavController().navigate(R.id.action_readingStepsFragment_to_manualInputFragment)
            }
        }

        water_meter_button.setOnClickListener() {
            sharedViewModel.meterIndex = 2
            if(sharedViewModel.isCameraAllowed){
                startMeterReading(2)
            }else{
                findNavController().navigate(R.id.action_readingStepsFragment_to_manualInputFragment)
            }
        }

        contact_button.setOnClickListener() {
            findNavController().navigate(R.id.action_readingStepsFragment_to_contactDetailsFragment)
        }

        nextButton.setOnClickListener() {
            Log.d("viewmodel",  Gson().toJson(
                PostModelRecord(
                sharedViewModel.utilization_code.value!!.verificationCode,
                sharedViewModel.meterData.toTypedArray(),
                sharedViewModel.userData.value!!.firstName,
                sharedViewModel.userData.value!!.secondName,
                sharedViewModel.userData.value!!.email,
                sharedViewModel.userData.value!!.phone,
                sharedViewModel.userData.value!!.street,
                sharedViewModel.userData.value!!.houseNumber,
                sharedViewModel.userData.value!!.postcode,
                sharedViewModel.userData.value!!.city,
                sharedViewModel.userData.value!!.floor,
                sharedViewModel.userData.value!!.sendCopy,
                sharedViewModel.userData.value!!.readingReason
            ).toString()))
                findNavController().navigate(R.id.action_readingStepsFragment_to_dataCheckingFragment)
        }

    }

    private fun startMeterReading(meterIndex: Int){
        val intent: Intent = Intent(this.requireActivity(), MeterScanningActivity::class.java)
            .putExtra(MeterReadingActivity.EXTRA_INTEGER_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(
                MeterReadingActivity.EXTRA_FRACTION_DIGITS,
                MeterReadingFragment.AUTOMATIC
            )
            .putExtra(
                MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS,
                MeterReadingFragment.AUTOMATIC
            )
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_UNREADABLE_COUNTER, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_AFTER_LAST_DETECTION, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT, 0)
            .putExtra(MeterReadingActivity.EXTRA_ALLOWS_ROTATION, true)
            .putExtra(MeterReadingActivity.EXTRA_ZOOM, 1.3)
            .putExtra(MeterReadingActivity.EXTRA_IS_RESULTS_OVERLAY_VISIBLE, true)
            .putExtra(MeterReadingActivity.EXTRA_IS_DEBUG_OVERLAY_VISIBLE, BuildConfig.DEBUG)

        intent.putExtra("ViewModel", ViewModelData(
            sharedViewModel.userData.value,
            sharedViewModel.co2_data.value,
            sharedViewModel.meterData,
            sharedViewModel.utilization_code.value,
            sharedViewModel.meterValue.value,
            sharedViewModel.faq.value,
            sharedViewModel.heat_meter_step,
            sharedViewModel.water_meter_step,
            sharedViewModel.heat_allocator_step,
            sharedViewModel.contact_step,
            sharedViewModel.steps_finished,
            sharedViewModel.meterIndex,
            sharedViewModel.isCameraAllowed
        )
        )

        startActivity(intent)
    }


}