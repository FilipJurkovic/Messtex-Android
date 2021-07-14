package com.messtex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moreActionButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_aboutFragment)
        }

        faqButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_faqFragment)
        }

        contactButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_contactFragment)
        }

        contactFormButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_contactFormFragment)
        }

        privacyButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_privacyFragment)
        }

        imprintButton.setOnClickListener(){
            findNavController().navigate(R.id.action_moreFragment2_to_imprintFragment)
        }

        shareLayoutButton.setOnClickListener(){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "Sharing the Messtex App")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

    }

}