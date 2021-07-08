package com.messtex.ui.fragments.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.messtex.R
import com.messtex.data.models.UserData
import kotlinx.android.synthetic.main.fragment_step_success.*


class StepSuccess : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
        button.setOnClickListener(){
//            findNavController().navigate(R.id.action_stepSuccess_to_stepUserInfo)
        }

    }
    
}