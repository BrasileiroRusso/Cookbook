package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Recipe

class RecipeViewModel(
    val recipeId: String,
    val categoryId: String,
    val title: String,
    val description: String,
    val recipe: String,
    val authorName: String,
    val imagePath: String
) {
    object Mapper{
        fun map(recipe: Recipe) =
            RecipeViewModel(
                recipe.recipeId,
                recipe.category_id,
                recipe.title,
                recipe.description,
                recipe.recipe,
                recipe.authorName,
                recipe.imagePath
            )
    }
}