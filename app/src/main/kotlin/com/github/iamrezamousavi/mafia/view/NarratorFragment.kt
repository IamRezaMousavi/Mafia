package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.databinding.FragmentNarratorBinding
import com.github.iamrezamousavi.mafia.view.adapter.NarratorAdapter
import com.github.iamrezamousavi.mafia.viewmodel.NarratorViewModel

class NarratorFragment : Fragment() {

    private lateinit var binding: FragmentNarratorBinding

    private val narratorViewModel: NarratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNarratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val narratorAdapter = NarratorAdapter(requireContext()) {
            narratorViewModel.updateItem(it)
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = narratorAdapter
        }

        narratorViewModel.playerRoleList.observe(viewLifecycleOwner) {
            narratorAdapter.submitList(it)
        }
    }
}
