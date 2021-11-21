package ru.rumigor.cookbook.ui.recipeDetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipeFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject
import kotlin.properties.Delegates

class RecipeDetailsFragment : AbsFragment(R.layout.recipe_fragment), RecipeDetailsView {

    companion object Factory {
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

    @Inject
    lateinit var router: Router

    @Suppress("unused")
    private val presenter: RecipeDetailsPresenter by moxyPresenter {
        RecipeDetailsPresenter(
            recipeId,
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            router = router
        )
    }

    private val ui: RecipeFragmentBinding by viewBinding()

    override fun showRecipe(recipe: RecipeViewModel) {
        context?.let {
            Glide.with(it)
                .load(recipe.imagePath)
                .into(ui.dishPic)
        }

        ui.recipeTitle.text = recipe.title

        ui.recipeDetails.text = recipe.recipe

        ui.authorName.text = "Автор: " + recipe.user.username

        ui.authorEmail.text = "E-mail: " + recipe.user.email

        ui.fabUpdate.setOnClickListener {
            presenter.updateRecipe(recipe)
        }
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }


}