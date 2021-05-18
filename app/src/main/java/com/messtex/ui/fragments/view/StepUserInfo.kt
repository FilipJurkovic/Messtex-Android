package com.messtex.ui.fragments.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.messtex.R
import com.messtex.ui.fragments.viewmodel.StepUserInfoViewModel

class StepUserInfo : Fragment() {

    companion object {
        fun newInstance() = StepUserInfo()
    }

    private lateinit var infoViewModel: StepUserInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_user_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        infoViewModel = ViewModelProvider(this).get(StepUserInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}