package com.gopenux.catbreed.ui.screens

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gopenux.catbreed.databinding.FragmentCatBreedDetailBinding
import com.gopenux.catbreed.domain.models.Cat
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
        binding.pais.text = cat.origin
        binding.adaptabilidad.text = cat.intelligence
        binding.adaptabilidad.text = cat.adaptability
        binding.descripcion.text = cat.description
        Glide.with(requireContext()).load(cat.urlImage).centerCrop().into(binding.imageViewPhoto)

        binding.appBar.toolbar.root.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}