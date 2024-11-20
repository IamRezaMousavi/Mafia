package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.github.iamrezamousavi.mafia.data.local.getLanguage
import com.github.iamrezamousavi.mafia.data.local.preferences
import com.github.iamrezamousavi.mafia.data.local.saveLanguage
import com.github.iamrezamousavi.mafia.data.model.Language
import com.github.iamrezamousavi.mafia.data.model.toNativeLanguage
import com.github.iamrezamousavi.mafia.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLanguageSpinner()
    }

    private fun setupLanguageSpinner() {
        val currentLanguage = requireContext().preferences.getLanguage()
        val items = Language
            .entries
            .map { it.nativeName }
            .sorted()
            .let { languages ->
                listOf(currentLanguage.nativeName) +
                    languages.filter { it != currentLanguage.nativeName }
            }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.langSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val newLanguage = selectedItem.toNativeLanguage()
                    val currentLang = requireContext().preferences.getLanguage()
                    if (newLanguage != currentLang) {
                        if (newLanguage != null) {
                            requireContext().preferences.saveLanguage(newLanguage)
                            setSelection(0)
                        }
                    }
                }

                @Suppress("EmptyFunctionBlock")
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
}
