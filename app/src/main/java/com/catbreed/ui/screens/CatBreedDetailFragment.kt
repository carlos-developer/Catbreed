package com.catbreed.ui.screens

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.catbreed.domain.models.Cat
import com.catbree.R
import com.catbree.databinding.FragmentCatBreedDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatBreedDetailFragment : Fragment() {

    private lateinit var binding: FragmentCatBreedDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBreedDetailBinding.inflate(inflater, container, false)
        AppCompatDelegate.setDefaultNightMode(UiModeManager.MODE_NIGHT_NO)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = Cat(
            arguments?.getString("name"),
            arguments?.getString("adaptability"),
            arguments?.getString("origin"),
            arguments?.getString("intelligence"),
            arguments?.getString("urlImage"),
            arguments?.getString("lifeSpan"),
            arguments?.getString("description")
        )

        binding.appBar.toolbar.toolbarTitle.text = cat.name
        binding.country.text =
            String.format(requireContext().getString(R.string.name_country), cat.origin)
        binding.intelligence.text = String.format(
            requireContext().getString(R.string.intelligence_detail),
            cat.intelligence
        )
        binding.adaptability.text =
            String.format(requireContext().getString(R.string.adaptability), cat.adaptability)
        binding.lifeSpan.text =
            String.format(requireContext().getString(R.string.life_span), cat.lifeSpan)
        binding.description.text =
            String.format(requireContext().getString(R.string.description), cat.description)
        Glide.with(requireContext()).load(cat.urlImage).fitCenter().into(binding.imageViewCatDetail)
        binding.imageViewCatDetail.setPadding(0, 0, 0, 0)

        binding.appBar.toolbar.root.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}