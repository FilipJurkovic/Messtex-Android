package com.messtex.ui.main.view.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.florent37.expansionpanel.ExpansionLayout
import com.messtex.R
import com.messtex.data.models.QuestionModel
import com.messtex.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.faq_card_layout.view.*
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

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

        if (sharedViewModel.scanningSuccessful) {
            startReadingButton.isVisible = false
            postScanningCard.isVisible = true
            postScanningButton.isVisible = true
        } else {
            startReadingButton.isVisible = true
            postScanningCard.isVisible = false
            postScanningButton.isVisible = false
        }
//
        sharedViewModel.co2_data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //co2_livedata.text = it.co2Level.toString()
                co2_livedata.text = "${it.co2Level.toString()} CO₂"
            }

        })

        startReadingButton.setOnClickListener {
            if (!checkPermission(requireContext(), permission)) {
                requestPermissions(arrayOf(permission), 10)
            }
            sharedViewModel.isCameraAllowed = checkPermission(requireContext(), permission)
            findNavController().navigate(R.id.action_home2_to_codeReadingFragment)
        }

        postScanningButton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_aboutFragment)
        }

        waterTip.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_waterTipFragment)
        }

        heatTip.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_heatTipFragment)
        }

        EnvironmentTip.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_environmentTipFragment)
        }

        shareButton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_contactFormFragment)
        }

        val inflater = LayoutInflater.from(this.requireContext())
        val faqSubarray = sharedViewModel.faq.value?.faqs?.copyOfRange(0, 3)
            ?: when (sharedViewModel.language_code) {
                "en" -> arrayOf(
                    QuestionModel(
                        1,
                        "When do I have to read the meters?",
                        "The meter readings currently have to be done once a year. We will inform you in advance when it is due again. Afterwards, you will have a time period of 2 weeks for entering your meter readings."
                    ),
                    QuestionModel(
                        2,
                        "Why should I use the Messtex app?",
                        "The Messtex app helps you enter meter readings quickly, conveniently, as well as cost- and time-efficiently from home without the necessity of waiting for a service technician."
                    ),
                    QuestionModel(
                        3,
                        "Why do I need to read my meter?",
                        "Your meter reading is used to determine your annual consumption. If you provide us with your actual meter reading, you will receive a fair and comprehensible bill.\n" +
                                "If you do not report your meter reading, it will be estimated. Then your bill will differ from your actual consumption in any case."
                    )
                )

                "de" -> arrayOf(
                    QuestionModel(
                        1,
                        "Wann muss ich die Zähler ablesen?",
                        "Die Zählerstände müssen aktuell einmal im Jahr abgelesen werden. Wir informieren Sie rechtzeitig, wenn es wieder soweit ist. Sie haben dann einen Zeitraum von 2 Wochen, in dem Sie Ihre Zählerstände eintragen können."
                    ),
                    QuestionModel(
                        2,
                        "Warum mit der Messtex App ablesen?",
                        "Die Messtex-App hilft Ihnen die Zählerstände schnell, bequem, kostensparend und zeiteffizient von Zuhause aus einzutragen."
                    ),
                    QuestionModel(
                        3,
                        "Warum muss ich meinen Zählerstand ablesen?",
                        "Mit Ihrem Zählerstand wird Ihr Jahresverbrauch ermittelt. Wenn Sie uns Ihren tatsächlichen Zählerstand mitteilen, erhalten Sie eine faire und nachvollziehbare Abrechnung.\n" +
                                "Sollten Sie Ihren Zählerstand nicht melden, wird er geschätzt. Dann wird Ihre Rechnung in jedem Fall von Ihrem tatsächlichen Verbrauch abweichen."
                    )
                )

                else -> arrayOf()
            }
        val questionArray = sharedViewModel.faq.value?.faqs?.copyOfRange(0, 3)
        for (i in faqSubarray.indices) {
            val toAdd: View = inflater.inflate(R.layout.faq_card_layout, homeFaq, false)

            if (i == faqSubarray.lastIndex) {
                toAdd.questionBreak.isVisible = false
            }

            toAdd.card_question.text = faqSubarray[i].question
            toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.card_answer.text = faqSubarray[i].answer
            toAdd.card_answer.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)

            toAdd.expansionLayout.addListener { expansionLayout: ExpansionLayout, b: Boolean ->
                if (expansionLayout.isExpanded) {
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_ParagraphBold)
                    toAdd.questionBreak.isVisible = false

                    var index: Int = 0
                    homeFaq.forEach {
                        if(index != i) {
                            it.expansionLayout.collapse(true)
                        }

                        index++
                    }
                } else {
                    toAdd.card_question.setTextAppearance(R.style.TextAppearance_Messtex_Paragraph)
                    if (i != faqSubarray.lastIndex) {
                        toAdd.questionBreak.isVisible = true
                    }
                }
            }
            homeFaq.addView(toAdd)
        }

        faqMoreButton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_faqFragment)
        }

    }

    private fun checkPermission(context: Context, permissionArray: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionArray
        ) == PackageManager.PERMISSION_GRANTED
    }


}