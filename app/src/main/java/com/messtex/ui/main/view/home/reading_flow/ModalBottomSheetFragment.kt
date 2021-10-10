package com.messtex.ui.main.view.home.reading_flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.messtex.R
import com.messtex.ui.main.viewmodel.MainViewModel

class ModalBottomSheetFragment : BottomSheetDialogFragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_modal_bottom_sheet, container, false)
    }

}