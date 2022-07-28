package com.gopenux.catbreed.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gopenux.catbreed.domain.interactors.DataSourceCallback
import com.gopenux.catbreed.domain.interactors.FetchCatBreedUseCase
import com.gopenux.catbreed.domain.models.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatBreedViewModel
@Inject
internal constructor(
    private val fetchCatBreedUseCase: FetchCatBreedUseCase,
) : ViewModel() {

    private var _catBreed = MutableLiveData<DataState<List<Cat>>>()
    val catBreed: LiveData<DataState<List<Cat>>> get() = _catBreed

    fun fetchListBreedCat() {
        _catBreed.value = DataState.Loading
        fetchCatBreedUseCase.execute(viewModelScope, object : DataSourceCallback<List<Cat>> {
            override fun onSuccess(result: List<Cat>) {
                viewModelScope.launch(Dispatchers.Main) {
                    _catBreed.value = DataState.Success(result)
                }
            }

            override fun onFail(e: Exception) {
                _catBreed.value = DataState.Error(e)
            }
        })
    }
}