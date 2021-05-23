package com.messtex.ui.fragments.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.messtex.R
import com.messtex.data.models.UserData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.step_user_info_fragment.*


class StepUserInfo : Fragment() {

    companion object {
        fun newInstance() = StepUserInfo()
    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_user_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
        sharedViewModel.userData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                firstNameText.setText(it.firstName)
                lastNameText.setText(it.secondName)
                homeNumerText.setText(it.houseNumber.toString())
                postcodeText.setText(it.postcode.toString())
                cityText.setText(it.city)
                addressText.setText(it.street)
            }

        })

        submitUserInfoButton.setOnClickListener() {
            sharedViewModel.userData.postValue(
                UserData(
                    firstNameText.text.toString(),
                    lastNameText.text.toString(),
                    homeNumerText.text.toString().toInt(),
                    postcodeText.text.toString().toInt(),
                    cityText.text.toString(),
                    addressText.text.toString()
                )
            )
            findNavController().navigate(R.id.action_stepUserInfo_to_stepMeterInfo)
        }

    }

}