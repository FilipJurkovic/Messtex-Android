package com.messtex

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.contact_confirmation_layout.view.*
import kotlinx.android.synthetic.main.fragment_data_checking.*
import kotlinx.android.synthetic.main.fragment_data_checking.sendButton
import kotlinx.android.synthetic.main.meter_confirmation_layout.view.*
import kotlinx.android.synthetic.main.reason_of_reading_layout.view.*
import kotlinx.coroutines.*


@DelicateCoroutinesApi
class DataCheckingFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_data_checking, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val inflater = LayoutInflater.from(this.requireContext())
        val meterArray = sharedViewModel.meterData
        val userData = sharedViewModel.userData.value!!

        val addContact: View = inflater.inflate(R.layout.contact_confirmation_layout, confirmation_list, false)

        for (i in meterArray.indices){
            val toAdd: View = inflater.inflate(R.layout.meter_confirmation_layout, confirmation_list, false)

            toAdd.confirmation_meter_icon.setImageResource(sharedViewModel.getMeterIcon(meterArray[i].counterType))
            toAdd.confirmation_meter_name.text = userData.meters[i].counterTypeName
            toAdd.confirmation_meter_number.text = "Nr. ${meterArray[i].counterNumber}"
            toAdd.confirmation_meter_value.text = meterArray[i].counterValue.toString()
            confirmation_list.addView(toAdd)
        }

        addContact.name.text = "${userData.firstName} ${userData.lastName}"
        addContact.email.text = userData.email
        addContact.address.text = "${userData.street} ${userData.houseNumber}"
        addContact.city.text = "${userData.postcode} ${userData.city}"
        confirmation_list.addView(addContact)

        dataCheckingBackButton.setOnClickListener(){
            findNavController().navigateUp()
        }

        sendButton.setOnClickListener(){
            sharedViewModel
            val dialog : ProgressDialog = ProgressDialog.show(this.requireContext(), "", "Loading...", true)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    sharedViewModel.sendReadings()
                    MainScope().launch {
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_dataCheckingFragment_to_scanningSuccessFragment)
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }

        }


    }

}