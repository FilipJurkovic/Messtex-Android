package com.messtex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.UserData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_contact_details.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*


class ContactDetailsFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contactDetailsBackButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        val rGroup = reasonOfReading as RadioGroup
        val readingReason: RadioButton =
            rGroup.findViewById<View>(rGroup.checkedRadioButtonId) as RadioButton


        contactDetailsNextButton.setOnClickListener() {
            sharedViewModel.userData.value = UserData(
                firstNameInput.text.toString(),
                surnameInput.text.toString(),
                emailInput.text.toString(),
                phoneNumberInput.text.toString(),
                streetInput.text.toString(),
                houseNumberInput.text.toString(),
                plzInput.text.toString().toInt(),
                cityInput.text.toString(),
                floorInput.text.toString(),
                emailCopy.isChecked,
                readingReason.text.toString()
            )
            findNavController().navigate(R.id.action_contactDetailsFragment_to_readingStepsFragment)
        }

    }

}