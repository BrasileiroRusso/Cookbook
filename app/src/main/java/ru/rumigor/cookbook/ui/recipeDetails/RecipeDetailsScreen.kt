package ru.rumigor.cookbook.ui.recipeDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class RecipeDetailsScreen (private val recipeId: String): FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment = RecipeDetailsFragment.newInstance(recipeId)
}