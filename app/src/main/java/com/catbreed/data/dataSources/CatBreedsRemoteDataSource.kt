package com.catbreed.data.dataSources

import com.catbreed.domain.models.Cat
import com.google.gson.JsonParser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CatBreedsRemoteDataSource @Inject internal constructor(
    private val catBreedMapper: CatBreedMapper,
) {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    suspend fun fetchAllCatBreeds(): List<Cat>? {
        var listCat: List<Cat>? = listOf()
        try {
            val call =
                getRetrofit().create(ApiCatBreedService::class.java).fetchAllCatsBreed("v1/breeds")
            val breedCat = call.execute()
            if (breedCat.isSuccessful) {
                if (breedCat.body() != null) {
                    val byteArray = breedCat.body()?.bytes() ?: byteArrayOf()
                    val jsonArray = JsonParser.parseString(String(byteArray)).asJsonArray
                    listCat = catBreedMapper.jsonArrayToCatBreedList(jsonArray)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listCat
    }
}