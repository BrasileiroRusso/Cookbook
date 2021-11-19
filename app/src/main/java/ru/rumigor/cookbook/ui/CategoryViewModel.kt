package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe

class CategoryViewModel(
    val id: String,
    val tittle: String
) {
    object Mapper{
        fun map(category: Category) =
            CategoryViewModel(
                category.id,
                category.title
            )
    }
}