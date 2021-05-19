package com.messtex.ui.fragments.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.messtex.R
import com.messtex.data.models.localdb.User
import com.messtex.ui.fragments.viewmodel.StepUserInfoViewModel
import kotlinx.android.synthetic.main.step_user_info_fragment.*

class StepUserInfo : Fragment() {

    companion object {
        fun newInstance() = StepUserInfo()
    }

    private lateinit var userViewModel: StepUserInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_user_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(StepUserInfoViewModel::class.java)

        userViewModel.userData.observe(this, Observer {
            firstNameText.setText(it.firstName)
            lastNameText.setText(it.secondName)
            emailText.setText(it.emailAddress)
            postcodeText.setText(it.postcode)
            cityText.setText(it.city)
            addressText.setText(it.street)
        })

        submitUserInfoButton.setOnClickListener() {
            userViewModel.onSubmitted(
                User(
                    0,
                    firstNameText.text.toString(),
                    lastNameText.text.toString(),
                    emailText.text.toString(),
                    postcodeText.text.toString().toInt(),
                    postcodeText.text.toString().toInt(),
                    cityText.text.toString(),
                    addressText.text.toString()
                )
            )
        }

    }

}