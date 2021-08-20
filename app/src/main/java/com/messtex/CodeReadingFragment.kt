package com.messtex

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.ContactFormData
import com.messtex.data.models.UtilizationData
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_contact_form.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CodeReadingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_reading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        warningText.isVisible = false
        verification_error_background.isVisible = false
        verification_input_background.isVisible = true

        backButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        previewButton.setOnClickListener() {
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        eyeglass_button.setOnClickListener() {
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        videoLink.setOnClickListener() {
            findNavController().navigate(R.id.action_codeReadingFragment_to_videoFragment)
        }


        nextButton.setOnClickListener() {
            sharedViewModel.utilization_code.value =
                UtilizationData(verificationCode = verificationCode.text.toString(), language = sharedViewModel.language_code)
            val dialog: ProgressDialog =
                ProgressDialog.show(this.requireContext(), "", "Loading...", true)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    sharedViewModel.checkVerificationCode()
                    MainScope().launch {
                        if(!(sharedViewModel.userData.value?.firstName == null)) {
                            dialog.dismiss()
                            warningText.isVisible = false
                            verification_error_background.isVisible = false
                            verification_input_background.isVisible = true
                            findNavController().navigate(R.id.action_codeReadingFragment_to_readingStepsFragment)
                        }else{
                            dialog.dismiss()
                            warningText.text = sharedViewModel.userData.value?.message
                            warningText.isVisible = true
                            verification_error_background.isVisible = true
                            verification_input_background.isVisible = false
                        }
                    }

                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                    MainScope().launch {
                        dialog.dismiss()
                        warningText.isVisible = true
                        verification_error_background.isVisible = true
                        verification_input_background.isVisible = false
                    }

                }
            }
        }

    }
}

