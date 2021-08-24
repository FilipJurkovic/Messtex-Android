package com.messtex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_contact_form.reportMessageInput
import kotlinx.android.synthetic.main.fragment_imprint.*
import kotlinx.android.synthetic.main.fragment_manual_input.*
import kotlinx.android.synthetic.main.fragment_privacy.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
import kotlinx.coroutines.launch

class ContactFormFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_contact_form, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (nameInput.text.toString() != "" && emailInput.text.toString() != "" && reportMessageInput.text.toString() != "") {
                    sendButton.setBackgroundResource(R.drawable.background_button_main)
                    sendButton.isEnabled = true
                } else{
                    sendButton.setBackgroundResource(R.drawable.background_button_main_disabled)
                    sendButton.isEnabled = false

                }
            }
        }

        contactFormBackButton.setOnClickListener() {
            findNavController().navigateUp()
        }


        nameInput.addTextChangedListener(watcher)
        emailInput.addTextChangedListener(watcher)
        reportMessageInput.addTextChangedListener(watcher)

        if (nameInput.text.toString() != "" && emailInput.text.toString() != "" && reportMessageInput.text.toString() != "") {
            sendButton.setBackgroundResource(R.drawable.background_button_main)
            sendButton.isEnabled = true
        } else {
            sendButton.setBackgroundResource(R.drawable.background_button_main_disabled)
            sendButton.isEnabled = false
        }

        sendButton.setOnClickListener() {
            lifecycleScope.launch {
                sharedViewModel.sendContactForm(
                    ContactFormData(nameInput.text.toString(),
                        emailInput.text.toString(),
                        subjectInput.text.toString(),
                        reportMessageInput.text.toString())

                )
            }
            findNavController().navigateUp()
        }
    }



}