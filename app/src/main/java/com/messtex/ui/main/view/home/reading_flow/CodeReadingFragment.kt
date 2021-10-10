package com.messtex.ui.main.view.home.reading_flow

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.BuildConfig
import com.messtex.R
import com.messtex.data.models.MeterConfigurationModel
import com.messtex.data.models.UtilizationData
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.ui.testing.TestingActivity
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CodeReadingFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_codeReadingFragment_to_home2)
        }
    }

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

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_codeReadingFragment_to_home2)
        }

        previewButton.setOnClickListener {
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        eyeglass_button.setOnClickListener {
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        videoLink.setOnClickListener {
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        verificationCode?.setOnPinEnteredListener { str ->
            if (str.toString().length == 6) {
                codeVerification()
            }
        }


        nextButton.setOnClickListener {
            if(BuildConfig.DEBUG){
                when(verificationCode.text.toString()){
                    "000000" -> startTestingActivity(MeterConfigurationModel(  // Water meters
                        "AUTO_DE_WATER_HOME",
                        fractionDigitsAuto = true,
                        integerDigitsAuto = true,
                        numberOfCountersAuto = true,
                        fractionDigits = null,
                        integerDigits = null,
                        numberOfCounters = null
                    ))
                    "111111" -> startTestingActivity(MeterConfigurationModel( // SensoStar U
                        "LCD",
                        fractionDigitsAuto = false,
                        integerDigitsAuto = false,
                        numberOfCountersAuto = true,
                        fractionDigits = 3,
                        integerDigits = 5,
                        numberOfCounters = null
                    ))
                    else -> codeVerification()
                }
            } else{
                codeVerification()
            }
        }

    }

    private fun startTestingActivity(configuration: MeterConfigurationModel){
        val intent = Intent(this.context, TestingActivity::class.java)
        intent.putExtra("configuration", configuration)
        startActivity(intent)
        this.requireActivity().finish()
    }

    private fun codeVerification() {
        sharedViewModel.utilization_code.value =
            UtilizationData(
                verificationCode = verificationCode.text.toString(),
                language = sharedViewModel.language_code
            )
        val dialog: ProgressDialog =if(sharedViewModel.language_code == "en") {
            ProgressDialog.show(this.requireContext(), "", "Will be checked...", true)
        }else{
            ProgressDialog.show(this.requireContext(), "", "Wird geprüft...", true)
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                sharedViewModel.checkVerificationCode()
                MainScope().launch {
                    if (sharedViewModel.userData.value?.firstName != null) {
                        dialog.dismiss()
                        warningText.isVisible = false
                        verification_error_background.isVisible = false
                        verification_input_background.isVisible = true
                        findNavController().navigate(R.id.action_codeReadingFragment_to_readingStepsFragment)

                    } else {
                        dialog.dismiss()
                        warningText.text = sharedViewModel.userData.value?.message
                        warningText.isVisible = true
                        verification_error_background.isVisible = true
                        verification_input_background.isVisible = false
                    }
                    verificationCode.setText("")
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

