package com.catbreed.domain.interactors

import com.catbreed.data.repositories.CatBreedRepository
import com.catbreed.domain.models.Cat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FetchCatBreedUseCase @Inject internal constructor(
    private val catBreedRepository: CatBreedRepository,
) {
    fun execute(
        viewModelScope: CoroutineScope,
        dataSourceCallback: DataSourceCallback<List<Cat>>
    ) {
        TaskAllCatBreed(viewModelScope, dataSourceCallback).doInAnotherThread()
    }

    private inner class TaskAllCatBreed(
        val viewModelScope: CoroutineScope,
        val dataSourceCallback: DataSourceCallback<List<Cat>>
    ) {
        fun doInAnotherThread() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val listCats: List<Cat> = catBreedRepository.fetchAllCatBreeds().orEmpty()
                    dataSourceCallback.onSuccess(listCats)
                } catch (e: Exception) {
                    dataSourceCallback.onFail(e)
                }
            }
        }
    }
}