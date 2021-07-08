package com.messtex

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*

class ReadingStepsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val sharedViewModel: MainViewModel by activityViewModels()
    private var route : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_steps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (sharedViewModel.isCameraAllowed){
            route = R.id.action_readingStepsFragment_to_meterReadingFragment
        }else{
            route = R.id.action_readingStepsFragment_to_manualInputFragment
        }
//
        heat_meter_step_success.isVisible = sharedViewModel.heat_meter_step
        heat_allcator_step_success.isVisible = sharedViewModel.heat_allocator_step
        water_meter_step_success.isVisible = sharedViewModel.water_meter_step
        contact_step_success.isVisible = sharedViewModel.contact_step

        stepsBackButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        heat_meter_button.setOnClickListener() {
                sharedViewModel.meterIndex = 0
                findNavController().navigate(route)
            }

        heat_allocator_button.setOnClickListener() {
            sharedViewModel.meterIndex = 1
            findNavController().navigate(route)
        }

        water_meter_button.setOnClickListener() {
            sharedViewModel.meterIndex = 2
            findNavController().navigate(route)
        }

        contact_button.setOnClickListener() {
            findNavController().navigate(R.id.action_readingStepsFragment_to_contactDetailsFragment)
        }

        nextButton.setOnClickListener() {
            if (sharedViewModel.steps_finished){
                findNavController().navigate(R.id.action_readingStepsFragment_to_dataCheckingFragment)
            }

        }

    }


}