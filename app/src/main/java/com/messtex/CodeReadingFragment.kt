package com.messtex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.UtilizationData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_home.*

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

        backButton.setOnClickListener(){
            findNavController().navigateUp()
        }

        previewButton.setOnClickListener (){
            findNavController().navigate(R.id.action_codeReadingFragment_to_exampleCodeFragment)
        }

        nextButton.setOnClickListener() {
            sharedViewModel.utilization_code.value = UtilizationData(verificationCode = verificationCode.toString())
            findNavController().navigate(R.id.action_codeReadingFragment_to_readingStepsFragment)
        }

    }
}
