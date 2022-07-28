package com.gopenux.catbreed.data.repositories

import com.gopenux.catbreed.data.dataSources.CatBreedsRemoteDataSource
import javax.inject.Inject

class CatBreedRepository @Inject internal constructor(
    private val catBreedsRemoteDataSource: CatBreedsRemoteDataSource,
) {
    suspend fun fetchAllCatBreeds() = catBreedsRemoteDataSource.fetchAllCatBreeds()
}