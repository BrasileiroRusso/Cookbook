package ru.rumigor.cookbook.ui.recipesList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

object RecipesListScreen: FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment = RecipesListFragment.newInstance()
}