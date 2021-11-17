package ru.rumigor.cookbook.data.di.modules

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.RecipeRepositoryImpl
import ru.rumigor.cookbook.ui.MainActivity
import ru.rumigor.cookbook.ui.main.MainFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import javax.inject.Singleton

@Module
interface CookBookModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindMainFragment(): MainFragment

    @ContributesAndroidInjector
    fun bindRecipeDetailsFragment(): RecipeDetailsFragment


    @Singleton
    @Binds
    fun bindRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository
}