package com.catbreed.ui.screens

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catbreed.domain.models.Cat
import com.catbreed.ui.CatBreedViewModel
import com.catbreed.ui.DataState
import com.catbree.R
import com.catbree.databinding.FragmentCatBreedBinding
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
                hideKeyBoard()
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

    private fun hideKeyBoard() {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also {
            it.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
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
                Toast.makeText(
                    requireContext(),
                    context?.getString(R.string.error_unexpected),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun emptyResult() {
        Toast.makeText(
            requireContext(),
            context?.getString(R.string.not_result),
            Toast.LENGTH_LONG
        ).show()
    }
}