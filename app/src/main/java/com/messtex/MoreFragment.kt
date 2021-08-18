package com.messtex

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.ui.onboarding.OnboardingActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_more.*
import java.util.*

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

        val languageArray: Array<String> = resources.getStringArray(R.array.language_array)

        language_spinner.adapter = ArrayAdapter<String>(this.requireContext(),R.layout.spinner_text, languageArray)

        language_spinner.setSelection(languageArray.indexOf(when(getLocale(this@MoreFragment.requireContext())){
            "en" ->  "English"
            "de" ->  "Deutsch"
            else -> "English"
        }))

        language_spinner.post(Runnable (){

            language_spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    var language_code = "en"

                    when(languageArray[position]){
                        "English" -> language_code = "en"
                        "Deutsch" -> language_code = "de"
                    }

                    if(language_code != getLocale(this@MoreFragment.requireContext())) {
                        setLocale(
                            this@MoreFragment.requireActivity(),
                            language_code
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        })

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

        restart_button.setOnClickListener(){
            val intent = Intent(activity, OnboardingActivity::class.java)
            startActivity(intent)
            this.requireActivity().finish()
        }



    }

    private fun getLocale(context: Context?): String? {
        val sharedPref = context?.getSharedPreferences("locale", Context.MODE_PRIVATE)
        return sharedPref?.getString("language", "en")
    }

    private fun setLocale(activity: Activity, languageCode: String?) {
        val sharedPref = requireActivity().getSharedPreferences("locale", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("language", languageCode)
        editor.apply()

        startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

}