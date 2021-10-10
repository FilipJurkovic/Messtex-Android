package com.messtex.ui.main.view.home.reading_flow

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.R
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.contact_confirmation_layout.view.*
import kotlinx.android.synthetic.main.fragment_data_checking.*
import kotlinx.android.synthetic.main.meter_confirmation_layout.view.*
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

        val addContact: View =
            inflater.inflate(R.layout.contact_confirmation_layout, confirmation_list, false)

        for (i in meterArray.indices) {
            val toAdd: View =
                inflater.inflate(R.layout.meter_confirmation_layout, confirmation_list, false)

            toAdd.confirmation_meter_icon.setImageResource(sharedViewModel.getMeterIcon(meterArray[i].counterType))
            toAdd.confirmation_meter_name.text = userData.meters?.get(i)?.counterTypeName
            toAdd.confirmation_meter_number.text = "Nr. ${meterArray[i].counterNumber}"
            toAdd.confirmation_meter_value.text = meterArray[i].counterValue.toString()
            confirmation_list.addView(toAdd)
        }

        addContact.name.text = "${userData.firstName} ${userData.lastName}"
        addContact.email.text = userData.email
        addContact.address.text = "${userData.street} ${userData.houseNumber}"
        addContact.city.text = "${userData.postcode} ${userData.city}"
        addContact.phone.text = userData.phone
        confirmation_list.addView(addContact)

        dataCheckingBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        sendButton.setOnClickListener {
            sharedViewModel

            showConfirmationDialog(requireActivity(), ({
                val dialog: ProgressDialog =if(sharedViewModel.language_code == "en") {
                    ProgressDialog.show(this.requireContext(), "", "Will be sent...", true)
                }else{
                    ProgressDialog.show(this.requireContext(), "", "Wird gesendet...", true)
                }
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        sharedViewModel.sendReadings()
                        MainScope().launch {

                            dialog.dismiss()
                            findNavController().navigate(R.id.action_dataCheckingFragment_to_scanningSuccessFragment)
                        }
                    } catch (e: Exception) {
                        Log.d("Error", e.toString())
                        dialog.dismiss()
                    }
                }
            }))


        }


    }

    fun showConfirmationDialog(activity: Activity, onConfirm: () -> Unit) {
        val dialog: Dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirmation_popup_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var cancelationButton: TextView = dialog.findViewById<TextView>(R.id.cancelationButton)
        var confirmationButton: TextView = dialog.findViewById<TextView>(R.id.confirmationButton)

        dialog.show()

        cancelationButton.setOnClickListener {
            dialog.dismiss()
        }

        confirmationButton.setOnClickListener {
            dialog.dismiss()
            onConfirm()
        }
    }


}