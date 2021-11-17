package ru.rumigor.cookbook.ui.recipeDetails

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel

class RecipeDetailsPresenter (
    private val recipeId: String,
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers
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
}