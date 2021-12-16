package com.messtex.ui.main.view.home.reading_flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.R
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_example_code.*

class ExampleCodeFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(sharedViewModel.language_code == "en"){
            exampleImage.setImageResource(R.drawable.example_eng)
        } else {
            exampleImage.setImageResource(R.drawable.example_de)
        }

        exitButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}