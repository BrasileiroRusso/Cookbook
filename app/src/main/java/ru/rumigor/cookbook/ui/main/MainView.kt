package ru.rumigor.cookbook.ui.main

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface MainView: ScreenView {
    @SingleState
    fun showRecipes(recipes: List<RecipeViewModel>)
}