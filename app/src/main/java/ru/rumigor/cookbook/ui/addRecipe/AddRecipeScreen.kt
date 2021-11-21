package ru.rumigor.cookbook.ui.addRecipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AddRecipeScreen(
    private val recipeId: String,
    private val recipeTitle: String,
    private val categoryId: String,
    private val description: String,
    private val recipe: String,
    private val url: String
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        AddRecipeFragment.newInstance(recipeId, recipeTitle, categoryId, description, recipe, url)
}