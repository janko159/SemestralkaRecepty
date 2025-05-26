package com.example.receptysemestralka.data

import androidx.room.*

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(recipe: RecipeEntity): Long

    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<RecipeEntity>
}
