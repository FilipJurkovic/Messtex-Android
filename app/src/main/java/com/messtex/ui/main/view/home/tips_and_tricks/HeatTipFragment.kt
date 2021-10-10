package com.messtex.ui.main.view.home.tips_and_tricks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.messtex.R
import kotlinx.android.synthetic.main.fragment_environment_tip.*
import kotlinx.android.synthetic.main.fragment_environment_tip.environmentBackButton
import kotlinx.android.synthetic.main.fragment_heat_tip.*

class HeatTipFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heat_tip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        heatBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}