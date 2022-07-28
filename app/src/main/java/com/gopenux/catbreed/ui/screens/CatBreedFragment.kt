package com.gopenux.catbreed.ui.screens

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gopenux.catbreed.databinding.FragmentCatBreedBinding
import com.gopenux.catbreed.domain.models.Cat
import com.gopenux.catbreed.ui.CatBreedViewModel
import com.gopenux.catbreed.ui.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatBreedFragment : Fragment(), ResultListener {

    private val viewModel: CatBreedViewModel by viewModels()
    private var adapterCatsBreed: AdapterCatsBreed? = null
    private lateinit var binding: FragmentCatBreedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBreedBinding.inflate(inflater, container, false)
        AppCompatDelegate.setDefaultNightMode(UiModeManager.MODE_NIGHT_NO)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchListBreedCat()
        viewModel.catBreed.observe(viewLifecycleOwner) { displayListCat(it) }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                adapterCatsBreed?.filter?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    viewModel.fetchListBreedCat()
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            viewModel.fetchListBreedCat()
            true
        }
    }

    private fun displayListCat(stateListCatsBreed: DataState<List<Cat>>) {
        when (stateListCatsBreed) {
            is DataState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is DataState.Success -> {
                binding.progressBar.visibility = View.GONE
                adapterCatsBreed =
                    AdapterCatsBreed(findNavController(), stateListCatsBreed.data, this)
                val layoutManager = LinearLayoutManager(context)
                binding.recycleViewCat.adapter = adapterCatsBreed
                binding.recycleViewCat.layoutManager = layoutManager
            }
            is DataState.Error -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun emptyResult() {
        Toast.makeText(
            requireContext(),
            "No hay resultados que concuerden con tu busqueda, Intentalo nuevamente.",
            Toast.LENGTH_LONG
        ).show()
    }
}