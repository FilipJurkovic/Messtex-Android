package com.messtex.ui.main.view.home.reading_flow

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.R
import com.messtex.data.models.UserData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_contact_details.*


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

        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                contactDetailsNextButton.setBackgroundResource(R.drawable.background_button_main)
                contactDetailsNextButton.isEnabled = true
            }
        }

        firstNameInput.setText(sharedViewModel.userData.value?.firstName)
        surnameInput.setText(sharedViewModel.userData.value?.lastName)
        emailInput.setText(sharedViewModel.userData.value?.email)
        phoneNumberInput.setText(sharedViewModel.userData.value?.phone)
        streetInput.setText(sharedViewModel.userData.value?.street)
        houseNumberInput.setText(sharedViewModel.userData.value?.houseNumber)
        plzInput.setText(sharedViewModel.userData.value?.postcode.toString())
        cityInput.setText(sharedViewModel.userData.value?.city)
        floorInput.setText(sharedViewModel.userData.value?.floor)


        firstNameInput.addTextChangedListener(watcher)
        surnameInput.addTextChangedListener(watcher)

        contactDetailsBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        contactDetailsNextButton.setOnClickListener {
            if(isValidEmail(emailInput.text.toString())){
                emailInput.error = null
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
                    sharedViewModel.userData.value!!.readingReason,
                    sharedViewModel.userData.value!!.meters,
                    false,
                    ""
                )
                sharedViewModel.sendCopy = emailCopy.isChecked
                findNavController().navigate(R.id.action_contactDetailsFragment_to_readingStepsFragment)
            }else{
                emailInput.error = "Invalid E-mail"
            }

        }

    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}