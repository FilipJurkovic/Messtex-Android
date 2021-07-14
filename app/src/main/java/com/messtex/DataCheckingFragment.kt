package com.messtex

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.ContactFormData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_contact_form.*
import kotlinx.android.synthetic.main.fragment_data_checking.*
import kotlinx.android.synthetic.main.fragment_data_checking.email
import kotlinx.android.synthetic.main.fragment_data_checking.sendButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DataCheckingFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_data_checking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        heatMeterId.text = sharedViewModel.meterData[0].counterNumber
        heatMeterValue.text = sharedViewModel.meterData[0].counterValue

        heatAllocatorId.text = sharedViewModel.meterData[1].counterNumber.toString()
        heatAllocatorValue.text = sharedViewModel.meterData[1].counterValue.toString()

        waterMeterId.text = sharedViewModel.meterData[2].counterNumber.toString()
        waterMeterValue.text = sharedViewModel.meterData[2].counterValue.toString()

        name.text = String.format("%s %s", sharedViewModel.userData.value?.firstName, sharedViewModel.userData.value?.secondName)
        email.text = sharedViewModel.userData.value?.email.toString()
        address.text = String.format("%s %s", sharedViewModel.userData.value?.street, sharedViewModel.userData.value?.houseNumber)
        city.text = String.format("%s %s", sharedViewModel.userData.value?.postcode, sharedViewModel.userData.value?.city)

        reason.text = sharedViewModel.userData.value?.readingReason.toString()

        dataCheckingBackButton.setOnClickListener(){
            findNavController().navigateUp()
        }

        sendButton.setOnClickListener(){
            sharedViewModel
            val dialog : ProgressDialog = ProgressDialog.show(this.requireContext(), "", "Loading...", true)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    sharedViewModel.co2_data.postValue(sharedViewModel.sendReadings())
                    Log.d("CO2 value", sharedViewModel.co2_data.value?.co2Level.toString())
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_dataCheckingFragment_to_scanningSuccessFragment)
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }

        }


    }

}