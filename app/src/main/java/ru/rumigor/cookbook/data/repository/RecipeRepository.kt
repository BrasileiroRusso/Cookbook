package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Observable
import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe

interface RecipeRepository {
    fun getRecipes(): Observable<List<Recipe>>
    fun getRecipe(recipeID: String): Observable<Recipe>
    fun getCategories(): Observable<List<Category>>
//    fun addRecipe(recipe: Recipe): Observable<ResponseBody>
}