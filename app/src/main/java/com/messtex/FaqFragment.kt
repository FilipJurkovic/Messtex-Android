package com.messtex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.messtex.data.models.QuestionModel
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_faq.*

class FaqFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
/*        val titles : ArrayList<String>? = null
        val answers : ArrayList<String>? = null

        for (item in sharedViewModel.faq.value!!.faqs){
            titles?.add(item.question)xxx
            answers?.add(item.answer)
        }*/

        val mockFAQ = arrayOf(QuestionModel(1, "Where can I find my warm water meter?", "You can usually find the water meters in your apartment in the kitchen, bathroom and / or toilet."),
            QuestionModel(2, "Where can I find my cold water meter?", "You can usually find the water meters in your apartment in the kitchen, bathroom and / or toilet.\""),
                    QuestionModel(3, "Where can I find my RMVs?", "You can usually find the RMWs in your apartment."))

    }

}