package ru.rumigor.cookbook.ui.recipeDetails

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.addRecipe.AddRecipeScreen

class RecipeDetailsPresenter (
    private val recipeId: String,
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
    private val router: Router
): MvpPresenter<RecipeDetailsView>() {
    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getRecipe(recipeId)
                .map(RecipeViewModel.Mapper::map)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipe,
                    viewState::showError
                )

    }

    override fun onDestroy() {
        disposables.clear()
    }

    fun updateRecipe(recipe: RecipeViewModel){
        router.navigateTo(AddRecipeScreen(recipe.recipeId, recipe.title, recipe.category.id.toString(),
        recipe.description, recipe.title, recipe.imagePath))
    }

}