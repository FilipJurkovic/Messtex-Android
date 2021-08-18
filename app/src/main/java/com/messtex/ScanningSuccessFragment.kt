package com.messtex

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_scanning_success.*

class ScanningSuccessFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_scanning_success, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val paragraph: String = getString(R.string.success_subtitle, sharedViewModel.userData.value?.firstName)

        successParagraph.text = paragraph

        co2_livedata.text = sharedViewModel.co2_data.value?.co2Level.toString()
        backToHomeButton.setOnClickListener(){
            sharedViewModel.scanningSuccessful = true
            findNavController().navigate(R.id.action_scanningSuccessFragment_to_home2)
        }

        learnMoreButton.setOnClickListener(){
            sharedViewModel.scanningSuccessful = true
            findNavController().navigate(R.id.action_scanningSuccessFragment_to_aboutFragment)
        }
    }


}