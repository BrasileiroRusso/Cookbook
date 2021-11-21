package ru.rumigor.cookbook.data.di.modules

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.RecipeRepositoryImpl
import ru.rumigor.cookbook.ui.MainActivity
import ru.rumigor.cookbook.ui.addRecipe.AddRecipeFragment
import ru.rumigor.cookbook.ui.main.MainFragment
import ru.rumigor.cookbook.ui.recipesList.RecipesListFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import javax.inject.Singleton

@Module
interface CookBookModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindRecipesListFragment(): RecipesListFragment

    @ContributesAndroidInjector
    fun bindRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    fun bindAddRecipeFragment(): AddRecipeFragment

    @ContributesAndroidInjector
    fun bindMainFragment(): MainFragment

    @Singleton
    @Binds
    fun bindRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository
}