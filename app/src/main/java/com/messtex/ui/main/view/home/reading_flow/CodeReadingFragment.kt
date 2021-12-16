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
import com.messtex.ui.meter_reading.MeterScanningActivity
import com.messtex.ui.testing.TestingActivity
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.coroutines.*

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
                when(str.toString()){
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
            }
        }


        nextButton.setOnClickListener {
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

        }

    }

    private fun startTestingActivity(configuration: MeterConfigurationModel){
        val intent: Intent = Intent(this.context, TestingActivity::class.java)
            .putExtra(MeterReadingActivity.EXTRA_METER_APPEARANCE, sharedViewModel.getCounterType(configuration.meterAppearance))
            .putExtra(MeterReadingActivity.EXTRA_INTEGER_DIGITS, if (configuration.integerDigitsAuto) MeterReadingFragment.AUTOMATIC else configuration.integerDigits)
            .putExtra(MeterReadingActivity.EXTRA_FRACTION_DIGITS, if (configuration.fractionDigitsAuto) MeterReadingFragment.AUTOMATIC else configuration.fractionDigits)
            .putExtra(
                MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS,
                if (configuration.numberOfCountersAuto) MeterReadingFragment.AUTOMATIC else configuration.numberOfCounters
            )
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_UNREADABLE_COUNTER, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_AFTER_LAST_DETECTION, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT, 0)
            .putExtra(MeterReadingActivity.EXTRA_ALLOWS_ROTATION, true)
            .putExtra(MeterReadingActivity.EXTRA_ZOOM, 1.3)
            .putExtra(MeterReadingActivity.EXTRA_IS_RESULTS_OVERLAY_VISIBLE, true)
            .putExtra(MeterReadingActivity.EXTRA_IS_DEBUG_OVERLAY_VISIBLE, BuildConfig.DEBUG)
        intent.putExtra("configuration", configuration)
        startActivity(intent)
        this.requireActivity().finish()
    }

    @DelicateCoroutinesApi
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

