package com.messtex

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_imprint.*
import kotlinx.android.synthetic.main.fragment_privacy.*
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

        contactFormBackButton.setOnClickListener() {
            findNavController().navigateUp()
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