package com.messtex

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.messtex.data.models.UserData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.step_user_info_fragment.*

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val sharedViewModel: MainViewModel by activityViewModels()
    private val permission = Manifest.permission.CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
        sharedViewModel.co2_data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //co2_livedata.text = it.co2Level.toString()
                co2_livedata.text = "12345.34"
            }

        })

        startReadingButton.setOnClickListener() {
            if (!checkPermission(requireContext(), permission)){
                requestPermissions(arrayOf(permission), 10)
            }
            sharedViewModel.isCameraAllowed = checkPermission(requireContext(), permission)
            findNavController().navigate(R.id.action_home2_to_codeReadingFragment)
        }

        shareButton.setOnClickListener() {
            findNavController().navigate(R.id.action_home2_to_moreFragment2)
        }

    }

    fun checkPermission(context: Context, permissionArray: String): Boolean{
        return ContextCompat.checkSelfPermission(context, permissionArray) == PackageManager.PERMISSION_GRANTED
    }

}