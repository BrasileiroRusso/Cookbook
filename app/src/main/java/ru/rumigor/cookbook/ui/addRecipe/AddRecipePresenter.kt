package ru.rumigor.cookbook.ui.addRecipe

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.CategoryViewModel

class AddRecipePresenter(private val recipeRepository: RecipeRepository,
                         private val schedulers: Schedulers
): MvpPresenter<AddRecipeView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
       disposables+=
           recipeRepository
               .getCategories()
               .map{categories -> categories.map(CategoryViewModel.Mapper::map)}
               .observeOn(schedulers.main())
               .subscribeOn(schedulers.background())
               .subscribe(
                   viewState::showCategories,
                   viewState::showError
               )
    }

    fun saveRecipe(){}
}