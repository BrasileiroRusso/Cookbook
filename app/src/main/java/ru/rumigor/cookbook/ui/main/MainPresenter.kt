package ru.rumigor.cookbook.ui.main

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.model.Recipe
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsScreen


class MainPresenter (
    private val recipeRepository: RecipeRepository,
    private val router: Router,
    private val schedulers: Schedulers
        ): MvpPresenter<MainView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getRecipes()
                .map { recipes-> recipes.map(RecipeViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipes,
                    viewState::showError
                )
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    fun displayRecipe(recipe: RecipeViewModel){
//        disposables +=
//            recipeRepository
//                .addRecipe(Recipe(recipe.recipeId, recipe.categoryId, recipe.title, recipe.description, recipe.recipe,
//                recipe.authorName, recipe.imagePath))
//                .observeOn(schedulers.main())
//                .subscribeOn(schedulers.background())
//                .subscribe()

        router.navigateTo(RecipeDetailsScreen(recipe.recipeId))
    }


}