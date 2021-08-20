package com.messtex

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.MeterReadingData
import com.messtex.data.models.MeterReceivingData
import com.messtex.data.models.ViewModelData
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import kotlinx.android.synthetic.main.fragment_contact_details.*
import kotlinx.android.synthetic.main.fragment_manual_input.*
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
        var counterValueFormatted: String = ""
        val meterIndex = sharedViewModel.meterIndex

        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (manualInputValue.text.toString() == "") {
                    nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main_disabled)
                    nextButtonManualInput.isEnabled = false
                } else{
                    nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main)
                    nextButtonManualInput.isEnabled = true
                }
            }
        }

        manualInputBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_manualInputFragment_to_readingStepsFragment)
        }

        manualInputValue.addTextChangedListener(watcher)
            if(manualInputValue.text.toString() == ""){
                nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main_disabled)
                nextButtonManualInput.isEnabled = false
            } else{
                nextButtonManualInput.setBackgroundResource(R.drawable.background_button_main)
                nextButtonManualInput.isEnabled = true
            }


        Log.d("Meter index", sharedViewModel.meterIndex.toString())


        counterTypeInput.setText(sharedViewModel.userData.value!!.meters?.get(meterIndex)?.counterType)
        meterTypeText.text = sharedViewModel.userData.value!!.meters?.get(meterIndex)?.counterTypeName
        counterNumberInput.setText(sharedViewModel.userData.value!!.meters?.get(meterIndex)?.counterNumber)

        val standardFormat = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date : Date = standardFormat.parse(Date().toString())
        readingDateInput.setText(formatter.format(date))

        manualInputValue.setText(sharedViewModel.meterValue.value?.replace(".", ""))
        nextButtonManualInput.setOnClickListener() {
            counterValueFormatted = if (manualInputValue.text != null) {
                manualInputValue.text?.substring(
                    0,
                    manualInputValue.text?.length!! - 2
                ) + "." + manualInputValue.text?.substring(
                    manualInputValue.text?.length!! - 2,
                    manualInputValue.text?.length!!
                )
            }else{
                "0.0"
            }

            sharedViewModel.meterValue.value = counterValueFormatted

            sharedViewModel.meterData.add(MeterReadingData(
                sharedViewModel.userData.value!!.meters?.get(meterIndex)!!.counterNumber,
                sharedViewModel.userData.value!!.meters?.get(meterIndex)!!.counterType,
                sharedViewModel.meterValue.value!!.toDouble(),
                reportMessageInput.text.toString()
            ))
            Log.d("Counter value", counterValueFormatted)

            sharedViewModel.readingStepsProgress[meterIndex] = true

            findNavController().navigate(R.id.action_manualInputFragment_to_readingStepsFragment)
        }
    }
}