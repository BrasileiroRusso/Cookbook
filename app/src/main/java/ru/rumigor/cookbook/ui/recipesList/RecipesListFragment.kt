package ru.rumigor.cookbook.ui.recipesList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipesFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeAdapter
import javax.inject.Inject

class RecipesListFragment: AbsFragment(R.layout.recipes_fragment), RecipesListView, RecipeAdapter.Delegate {

    companion object{
        fun newInstance(): Fragment = RecipesListFragment().arguments()
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Suppress("unused")
    private val presenter: RecipesListPresenter by moxyPresenter {
        RecipesListPresenter(
            router = router,
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    private val ui: RecipesFragmentBinding by viewBinding()
    private val recipeAdapter = RecipeAdapter(delegate = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.recipesList.adapter = recipeAdapter

    }

    override fun showRecipes(recipes: List<RecipeViewModel>) {
        recipeAdapter.submitList(recipes)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(
            requireContext(),
            "Sorry, something go wrong(",
            Toast.LENGTH_LONG
        ).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onRecipePicked(recipe: RecipeViewModel) {
            presenter.displayRecipe(recipe.recipeId)
    }


}