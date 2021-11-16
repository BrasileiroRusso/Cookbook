package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Recipe(
    @PrimaryKey
    @SerializedName("id")
    val recipeId: String,
    @ColumnInfo(name = "category")
    @SerializedName("category_id")
    val category_id: String,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String,
    @ColumnInfo(name = "recipe")
    @SerializedName("recipe")
    val recipe: String,
    @ColumnInfo(name = "author")
    @SerializedName("user_id")
    val authorName: String,
    @ColumnInfo(name = "imagepath")
    @SerializedName("imagepath")
    val imagePath: String
)
