package ru.rumigor.cookbook.ui.main

import com.github.terrakok.cicerone.Router
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers

class MainPresenter (
    private val recipeRepository: RecipeRepository,
    private val router: Router,
    private val schedulers: Schedulers
        ): MvpPresenter<MainView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }


}