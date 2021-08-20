package com.messtex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.messtex.data.models.PostModelRecord
import com.messtex.data.models.ViewModelData
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import kotlinx.android.synthetic.main.fragment_reading_steps.*
import kotlinx.android.synthetic.main.fragment_reading_steps.nextButton
import kotlinx.android.synthetic.main.reading_step_layout.view.*

class ReadingStepsFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_steps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//

        val inflater = LayoutInflater.from(this.requireContext())
        val stepsArray = sharedViewModel.userData.value?.meters

        for (i in (stepsArray?.indices!!)) {
            val toAdd: View =
                inflater.inflate(R.layout.reading_step_layout, reading_steps_layout, false)
                if (sharedViewModel.readingStepsProgress[i]) {
                    toAdd.readingIndex.text = ""
                    toAdd.readingIndexShape.setBackgroundResource(R.drawable.bullet_point)
                } else {
                    toAdd.readingIndex.text = (i + 1).toString()
                    toAdd.readingIndex.setTextAppearance(R.style.TextAppearance_Messtex_ParagraphBold)
                }


                toAdd.meterIcon.setImageResource(sharedViewModel.getMeterIcon(stepsArray[i].counterType))
                toAdd.meterName.text = stepsArray[i].counterTypeName

                toAdd.meterButton.setOnClickListener {
                    sharedViewModel.meterIndex = i
                    if (sharedViewModel.isCameraAllowed) {
                        startMeterReading(i)
                    } else {
                        findNavController().navigate(R.id.action_readingStepsFragment_to_manualInputFragment)
                    }
                }
                if (sharedViewModel.readingStepsProgress[i]) {
                    toAdd.meterButton.setBackgroundResource(R.drawable.background_button_step_change)
                    toAdd.meterButton.setTextColor(resources.getColor(R.color.light))
                    toAdd.meterButton.text = "Change"
                }


            reading_steps_layout.addView(toAdd)
        }

        val toAdd: View =
            inflater.inflate(R.layout.reading_step_layout, reading_steps_layout, false)

        if (sharedViewModel.userData.value?.firstName != null) {
            toAdd.readingIndex.text = ""
            toAdd.readingIndexShape.setBackgroundResource(R.drawable.bullet_point)

            toAdd.meterButton.setBackgroundResource(R.drawable.background_button_step_change)
            toAdd.meterButton.setTextColor(resources.getColor(R.color.light))
            toAdd.meterButton.text = "Change"

            sharedViewModel.readingStepsProgress[sharedViewModel.readingStepsProgress.size-1] = true
        }
        toAdd.meterIcon.isVisible = false
        toAdd.meterName.text = "Contact and contract data"
        toAdd.reading_steps_connector.isVisible = false

        toAdd.meterButton.setOnClickListener {
            findNavController().navigate(R.id.action_readingStepsFragment_to_contactDetailsFragment)
        }

        reading_steps_layout.addView(toAdd)


        if (sharedViewModel.meterData.size == sharedViewModel.userData.value?.meters?.size && sharedViewModel.userData.value?.firstName != null) {
            nextButton.setBackgroundResource(R.drawable.background_button_main)
            nextButton.isEnabled = true
        } else {
            nextButton.setBackgroundResource(R.drawable.background_button_main_disabled)
            nextButton.isEnabled = false
        }

        stepsBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        nextButton.setOnClickListener {
            Log.d(
                "viewmodel", Gson().toJson(
                    PostModelRecord(
                        sharedViewModel.utilization_code.value!!.verificationCode,
                        sharedViewModel.language_code,
                        sharedViewModel.meterData.toTypedArray(),
                        sharedViewModel.userData.value!!.firstName!!,
                        sharedViewModel.userData.value!!.lastName!!,
                        sharedViewModel.userData.value!!.email!!,
                        sharedViewModel.userData.value!!.phone!!,
                        true
                    ).toString()
                )
            )
            findNavController().navigate(R.id.action_readingStepsFragment_to_dataCheckingFragment)
        }

    }

    private fun startMeterReading(meterIndex: Int) {
        val intent: Intent = Intent(this.requireActivity(), MeterScanningActivity::class.java)
            .putExtra(MeterReadingActivity.EXTRA_INTEGER_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(
                MeterReadingActivity.EXTRA_FRACTION_DIGITS,
                MeterReadingFragment.AUTOMATIC
            )
            .putExtra(
                MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS,
                MeterReadingFragment.AUTOMATIC
            )
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_UNREADABLE_COUNTER, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_AFTER_LAST_DETECTION, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT, 0)
            .putExtra(MeterReadingActivity.EXTRA_ALLOWS_ROTATION, true)
            .putExtra(MeterReadingActivity.EXTRA_ZOOM, 1.3)
            .putExtra(MeterReadingActivity.EXTRA_IS_RESULTS_OVERLAY_VISIBLE, true)
            .putExtra(MeterReadingActivity.EXTRA_IS_DEBUG_OVERLAY_VISIBLE, BuildConfig.DEBUG)

        intent.putExtra(
            "ViewModel", ViewModelData(
                sharedViewModel.userData.value,
                sharedViewModel.co2_data.value,
                sharedViewModel.meterData,
                sharedViewModel.utilization_code.value,
                sharedViewModel.meterValue.value,
                sharedViewModel.faq.value,
                sharedViewModel.readingStepsProgress,
                meterIndex,
                sharedViewModel.isCameraAllowed
            )
        )

        startActivity(intent)
    }


}