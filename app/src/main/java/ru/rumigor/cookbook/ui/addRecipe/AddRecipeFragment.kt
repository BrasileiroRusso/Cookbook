package ru.rumigor.cookbook.ui.addRecipe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.AddrecipeViewBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.ServerResponseViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsPresenter
import javax.inject.Inject

class AddRecipeFragment : AbsFragment(R.layout.addrecipe_view), AddRecipeView {

    companion object Factory {
        private const val ARG_RECIPE_ID = "arg_recipe_id"
        private const val ARG_RECIPE_TITLE = "arg_recipe_title"
        private const val ARG_CATEGORY_ID = "arg_category_id"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_RECIPE = "arg_recipe"
        private const val ARG_URL = "arg_url"

        fun newInstance(recipeId: String, recipeTitle: String,
        recipeCategoryId: String, description: String, recipe: String, url: String): Fragment =
            AddRecipeFragment().arguments(ARG_RECIPE_ID to recipeId,
            ARG_RECIPE_TITLE to recipeTitle, ARG_CATEGORY_ID to recipeCategoryId, ARG_DESCRIPTION to description,
            ARG_RECIPE to recipe, ARG_URL to url)
    }
    private val recipeId: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_RECIPE_ID).orEmpty()
    }

    private val recipeTitle: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_RECIPE_TITLE).orEmpty()
    }

    private val recipeCategoryId: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_CATEGORY_ID).orEmpty()
    }

    private val description: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_DESCRIPTION).orEmpty()
    }

    private val recipe: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_RECIPE).orEmpty()
    }

    private val url: String by lazy{
        arguments?.getString(AddRecipeFragment.ARG_URL).orEmpty()
    }


    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var router: Router

    private val presenter: AddRecipePresenter by moxyPresenter {
        AddRecipePresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            router = router,
            recipeId = recipeId
        )
    }

    private val ui: AddrecipeViewBinding by viewBinding()

    private var categoryId = 1

    override fun showCategories(categories: List<CategoryViewModel>) {
        val categoriesList = mutableListOf<String>()
        for (category in categories) {
            categoriesList.add(category.tittle)
        }

        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ui.chooseCategory.adapter = spinnerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (recipeId != "0"){
            ui.newTitle.setText(recipeTitle)
            ui.newDescription.setText(description)
            ui.recipeDetails.setText(recipe)
            ui.url.setText(url)
            ui.chooseCategory.setSelection(categoryId)
        }
        ui.loadImage.setOnClickListener {
            if (ui.url.text.toString() != "") {
                context?.let {
                    Glide.with(it)
                        .load(ui.url.text.toString())
                        .into(ui.imageView)
                }
            }
        }
        ui.addRecipeButton.setOnClickListener {
            if ((ui.newTitle.text.toString() != "") && (ui.newDescription.text.toString() != "")
                    && (ui.recipeDetails.text.toString() != "")){
                val title = ui.newTitle.text.toString()
                val description = ui.newDescription.text.toString()
                val recipe = ui.recipeDetails.text.toString()
                val imagePath = ui.url.text.toString()
                categoryId = ui.chooseCategory.selectedItemPosition + 1
                presenter.saveRecipe(title, description, recipe, imagePath, categoryId)
            }
        }
    }

    override fun showAnswer(serverResponse: ServerResponseViewModel) {
        Toast.makeText(requireContext(), "new id: ${serverResponse.id}", Toast.LENGTH_LONG).show()
        presenter.showRecipe(serverResponse.id)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        Log.d("ERROR", error.message.toString())
    }


}