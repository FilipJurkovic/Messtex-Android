package com.messtex.ui.main.view.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.florent37.expansionpanel.ExpansionLayout
import com.messtex.R
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.faq_card_layout.view.*
import kotlinx.android.synthetic.main.fragment_faq.*

class FaqFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val inflater = LayoutInflater.from(this.requireContext())
        val questionArray = sharedViewModel.faq.value?.faqs
        for (i in questionArray?.indices!!) {
            val toAdd: View = inflater.inflate(R.layout.faq_card_layout, faq_layout, false)

            if (i == questionArray.lastIndex) {
                toAdd.questionBreak.isVisible = false
            }

            toAdd.card_question.text = questionArray[i].question
            toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.card_answer.text = questionArray[i].answer
            toAdd.card_answer.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.expansionLayout.addListener { expansionLayout: ExpansionLayout, b: Boolean ->
                if (expansionLayout.isExpanded) {
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_ParagraphBold)
                    toAdd.questionBreak.isVisible = false

                    var index: Int = 0
                    faq_layout.forEach {
                        if(index != i) {
                            it.expansionLayout.collapse(true)
                        }

                        index++
                    }
                } else {
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)
                    toAdd.questionBreak.isVisible = true
                }
            }

            faq_layout.addView(toAdd)
        }

        faqBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}