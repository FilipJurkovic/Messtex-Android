package com.messtex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_meter_reading.*
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.*

class ModalBottomSheetFragment : BottomSheetDialogFragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_modal_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bottomSheetIndex = sharedViewModel.meterIndex

        infoExitButton.setOnClickListener(){
            findNavController().navigateUp()
        }

        when (bottomSheetIndex) {
            0 -> {
                meterImage.setImageResource(R.drawable.watermeter_illustration)
                explanation.text = "Please enter your meter number first. It is usually located at the top of your counter and usually consists of 9 numbers (no letters). On your notification card you will find the last 4 digits and the respective room in which the meter is located as a little help"
                readingExplanation.text = "Then enter your meter reading in the corresponding field. Please enter both the numbers in front of the comma (here in the gray area) and the numbers after they come (here in the red area). The counter reading in this example is: 00984.562"
            }
            1 -> {
                meterImage.setImageResource(R.drawable.gasmeter_illustration)
                explanation.text = "Please enter your meter number first. It is usually located above or below the display and consists of 9 numbers (no letters). On your notification card you will find the last 4 digits and the respective room in which the meter is located as a small aid."
                readingExplanation.text = "To be able to read the counter reading, press the button on your counter. Your meter reading will now appear on the display. Please enter this in the corresponding field on the homepage. The counter reading in this example is: 14652"

            }
            2 -> {
                meterImage.setImageResource(R.drawable.electricity_illustration)
                explanation.text = "Please enter your meter number first. It is usually located at the top of your counter and usually consists of 9 numbers (no letters). On your notification card you will find the last 4 digits and the respective room in which the meter is located as a little help"
                readingExplanation.text = "To be able to read the counter reading, press the button on your counter. Your meter reading will now appear on the display. Please enter this in the corresponding field on the homepage. The counter reading in this example is: 14562."

            }
        }
    }

}