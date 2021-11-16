package ru.rumigor.cookbook.data.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.rumigor.cookbook.data.model.Recipe

interface CookbookApi {

    @GET("/recipes")
    fun getRecipes(): Single<List<Recipe>>
}