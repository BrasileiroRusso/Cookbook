package ru.rumigor.cookbook.data.api

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe

interface CookbookApi {

    @GET("/cookbook/rest/recipe")
    fun getRecipes(): Single<List<Recipe>>
    @GET("/cookbook/rest/recipe/{recipe_id}")
    fun getRecipe(@Path("recipe_id") id: String): Single<Recipe>
    @POST("/cookbook/rest/recipe")
    fun addRecipe(@Body recipe: Recipe): Single<ResponseBody>
    @GET("/cookbook/rest/category")
    fun getCategory(): Single<List<Category>>
}