package ru.rumigor.cookbook.ui.recipeDetails

import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipeFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject

class RecipeDetailsFragment: AbsFragment(R.layout.recipe_fragment), RecipeDetailsView {

    companion object Factory{
        private const val ARG_RECIPE_ID = "arg_recipe_id"

        fun newInstance(recipeId: String): Fragment =
            RecipeDetailsFragment().arguments(ARG_RECIPE_ID to recipeId)
    }

    private val recipeId: String by lazy{
        arguments?.getString(ARG_RECIPE_ID).orEmpty()
    }

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Suppress("unused")
    private val presenter: RecipeDetailsPresenter by moxyPresenter {
        RecipeDetailsPresenter(
            recipeId,
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    private val ui: RecipeFragmentBinding by viewBinding()

    override fun showRecipe(recipe: RecipeViewModel) {
        context?.let{
            Glide.with(it)
                .load(recipe.imagePath)
                .into(ui.imageView)
        }

        ui.recipeTitle.text = recipe.title

        ui.recipeDetails.text = recipe.recipe
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }


}