package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.databinding.FragmentNarratorBinding
import com.github.iamrezamousavi.mafia.view.adapter.NarratorAdapter

class NarratorFragment : Fragment() {

    private lateinit var binding: FragmentNarratorBinding

    private val mainViewModel: MainViewModel by activityViewModels()

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
            mainViewModel.updateNarratorItem(it)
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = narratorAdapter
        }

        mainViewModel.narratorUiState.observe(viewLifecycleOwner) {
            narratorAdapter.submitList(it.fullList)

            binding.apply {
                alivePeople.text = getString(R.string.alive_count, it.aliveCount)
                deadPeople.text = getString(R.string.dead_count, it.deadCount)

                aliveCitizen.text = getString(R.string.citizen_count, it.aliveCitizen)
                aliveMafia.text = getString(R.string.mafia_count, it.aliveMafia)
                aliveIndependent.text = getString(R.string.independent_count, it.aliveIndependent)

                deadCitizen.text = getString(R.string.citizen_count, it.deadCitizen)
                deadMafia.text = getString(R.string.mafia_count, it.deadMafia)
                deadIndependent.text = getString(R.string.independent_count, it.deadIndependent)

                updateNarratorCardColors(it.winner)
            }
        }

        binding.narratorToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    mainViewModel.applyAllPlayerAlive()
                    Toast.makeText(context, R.string.role_refresh, Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menuItemReset -> {
                    mainViewModel.hidePlayerRoles()
                    Toast.makeText(context, R.string.role_hide, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun updateNarratorCardColors(roleSide: RoleSide?) {
        val (bgColor, aliveColor, deadColor) = when (roleSide) {
            RoleSide.CITIZEN -> Triple(
                R.color.citizen_md_theme_primary,
                R.color.citizen_md_theme_onPrimary,
                R.color.citizen_md_theme_onPrimary
            )

            RoleSide.MAFIA -> Triple(
                R.color.mafia_md_theme_primary,
                R.color.mafia_md_theme_onPrimary,
                R.color.mafia_md_theme_onPrimary
            )

            RoleSide.INDEPENDENT -> Triple(
                R.color.independent_md_theme_primary,
                R.color.independent_md_theme_onPrimary,
                R.color.independent_md_theme_onPrimary
            )

            null -> Triple(
                R.color.md_theme_background,
                R.color.citizen_md_theme_primary,
                R.color.mafia_md_theme_primary
            )
        }

        binding.apply {
            narratorCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))
            alivePeople.setTextColor(ContextCompat.getColor(requireContext(), aliveColor))
            deadPeople.setTextColor(ContextCompat.getColor(requireContext(), deadColor))
        }
    }
}
