package com.messtex

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
        co2_livedata.text = sharedViewModel.co2_data.value?.co2Level.toString()
        backToHomeButton.setOnClickListener(){
            findNavController().navigate(R.id.action_scanningSuccessFragment_to_home2)
        }
        return inflater.inflate(R.layout.fragment_scanning_success, container, false)
    }


}