package ru.rumigor.cookbook.ui.main

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.rumigor.cookbook.ui.addRecipe.AddRecipeScreen
import ru.rumigor.cookbook.ui.recipesList.RecipesListScreen

class MainPresenter(
    private val router: Router
): MvpPresenter<MainView>() {

    fun displayRecipes(){
        router.navigateTo(RecipesListScreen)
    }

    fun addRecipe(){
        router.navigateTo(AddRecipeScreen("0", "", "", "", "", ""))
    }
}