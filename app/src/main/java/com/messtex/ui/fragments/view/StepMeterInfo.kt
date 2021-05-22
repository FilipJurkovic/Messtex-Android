package com.messtex.ui.fragments.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.messtex.R
import com.messtex.data.models.MeterData
import com.messtex.ui.fragments.viewmodel.StepMeterInfoViewModel
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.step_meter_info_fragment.*
import org.kodein.di.KodeinAware

class StepMeterInfo : Fragment()  {

    //private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_meter_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val meterTypeArray = resources.getStringArray(R.array.meter_types_array)
        meterTypeList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val data = sharedViewModel.meterData.value
                sharedViewModel.meterData.postValue(
                    MeterData(
                        data?.meterId,
                        data?.meterValue,
                        parent?.getItemAtPosition(position).toString()
                    )
                )
            }

        }

        sharedViewModel.meterData.observe(viewLifecycleOwner, Observer {
            meterIdText.setText(it.meterId.toString())
            meterReadingText.setText(it.meterValue.toString())
            if (it.meterType != null) meterTypeList.setSelection(meterTypeArray.indexOf(it.meterType))
        })


    }

}