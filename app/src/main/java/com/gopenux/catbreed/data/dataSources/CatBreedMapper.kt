package com.gopenux.catbreed.data.dataSources

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.gopenux.catbreed.domain.models.Cat
import javax.inject.Inject

class CatBreedMapper @Inject constructor() {

    fun jsonArrayToCatBreedList(jsonArray: JsonArray): List<Cat> {
        return jsonArray.map { jsonObjectToCat(it.asJsonObject) }
    }

    private fun jsonObjectToCat(jsonObject: JsonObject): Cat {

        var urlImage = ""
        if (jsonObject.has("image")) {
            val jsonObject = jsonObject.get("image").asJsonObject
            if (jsonObject.has("url")) {
                urlImage = jsonObject.get("url").asString
            }
        }
        return Cat(
            jsonObject.get("name").asString,
            jsonObject.get("adaptability").asString,
            jsonObject.get("origin").asString,
            (jsonObject.get("intelligence").asInt).toString(),
            urlImage,
            jsonObject.get("life_span").asString,
            jsonObject.get("description").asString
        )
    }
}