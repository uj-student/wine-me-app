package com.student.wine_me_up.wine_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WineDao {
    @Query("Select * from wine")
    fun getAllWines(): List<Wine>

    @Query("Select * from wine order by score desc limit 10")
    fun getTopScoreWine(): List<Wine>

    @Query("Select * from wine order by journalist_count desc limit 10")
    fun getTopNumberOfReviewers(): List<Wine>

    @Query("Select * from wine order by confidence_index desc limit 10")
    fun getHighestConfidence(): List<Wine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWine(wineResponse: Wine)

    @Query("select * from wine limit 1")
    fun isDatabasePopulated(): Wine?
}