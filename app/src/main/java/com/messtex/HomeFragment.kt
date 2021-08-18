package com.messtex

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.github.florent37.expansionpanel.ExpansionLayout
import com.messtex.data.models.QuestionModel
import com.messtex.data.models.UserData
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.faq_card_layout.view.*
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_home.*

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

        if (!sharedViewModel.scanningSuccessful){
            startReadingButton.isVisible = false
            postScanningCard.isVisible = true
            postScanningButton.isVisible = true
        }else{
            startReadingButton.isVisible = true
            postScanningCard.isVisible = false
            postScanningButton.isVisible = false
        }
//
        sharedViewModel.co2_data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //co2_livedata.text = it.co2Level.toString()
                co2_livedata.text = it.co2Level.toString()
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
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "Sharing the Messtex App")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        val inflater = LayoutInflater.from(this.requireContext())
        val faqSubarray = sharedViewModel.faq.value?.faqs?.copyOfRange(0, 3) ?: arrayOf(
            QuestionModel(1, "Where can I find my warm water meter?", "You can usually find the water meters in your apartment in the kitchen, bathroom and / or toilet."),
            QuestionModel(2, "Where can I find my cold water meter?", "You can usually find the water meters in your apartment in the kitchen, bathroom and / or toilet.\""),
            QuestionModel(3, "Where can I find my RMVs?", "You can usually find the RMWs in your apartment.")
        )
        val questionArray = sharedViewModel.faq.value?.faqs
        for (i in faqSubarray.indices){
            val toAdd: View = inflater.inflate(R.layout.faq_card_layout, homeFaq, false)

            if (i == faqSubarray.lastIndex){
                toAdd.questionBreak.isVisible = false
            }

            toAdd.card_question.text = faqSubarray[i].question
            toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.card_answer.text = faqSubarray[i].answer
            toAdd.card_answer.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.expansionLayout.addListener(){ expansionLayout: ExpansionLayout, b: Boolean ->
                if (expansionLayout.isExpanded){
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_ParagraphBold)
                    toAdd.questionBreak.isVisible = false
                }else{
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)
                    if(i != faqSubarray.lastIndex){
                        toAdd.questionBreak.isVisible = true
                    }
                }
            }
            homeFaq.addView(toAdd)
        }

        faqMoreButton.setOnClickListener() {
            findNavController().navigate(R.id.action_home2_to_faqFragment)
        }

    }

    fun checkPermission(context: Context, permissionArray: String): Boolean{
        return ContextCompat.checkSelfPermission(context, permissionArray) == PackageManager.PERMISSION_GRANTED
    }

}