package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Observable
import ru.rumigor.cookbook.data.api.CookbookApi
import ru.rumigor.cookbook.data.model.Recipe
import javax.inject.Inject

interface RecipeRepository {
    fun getRecipes(): Observable<List<Recipe>>
    fun getRecipe(recipeID: String): Observable<Recipe>
}