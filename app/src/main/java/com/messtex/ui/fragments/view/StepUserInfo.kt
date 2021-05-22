package com.messtex.ui.fragments.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.messtex.R
import com.messtex.data.models.localdb.User
import com.messtex.ui.fragments.viewmodel.StepUserInfoViewModel
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.step_user_info_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class StepUserInfo(override val kodein: Kodein) : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = StepUserInfo()
    }

    //private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_user_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel.userData.observe(viewLifecycleOwner, Observer {
            firstNameText.setText(it.firstName)
            lastNameText.setText(it.lastName)
            postcodeText.setText(it.postcode)
            cityText.setText(it.city)
            addressText.setText(it.street)
        })

        submitUserInfoButton.setOnClickListener() {
            sharedViewModel.onSubmitted(
                User(

                    firstNameText.text.toString(),
                    lastNameText.text.toString(),
                    postcodeText.text.toString().toInt(),
                    postcodeText.text.toString().toInt(),
                    cityText.text.toString(),
                    addressText.text.toString()
                )
            )
        }

    }

}