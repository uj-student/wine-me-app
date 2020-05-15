package com.student.wine_me_up.wine_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.student.wine_me_up.models.ReviewTypeDescription
import com.student.wine_me_up.models.WineReviewsModel

@Dao
interface WineDao {

    // WineEntity Queries
    @Query("Select * from WineEntity")
    fun getAllWines(): List<WineEntity>

    @Query("Select * from WineEntity order by score desc limit 10")
    fun getTopScoreWine(): List<WineEntity>

    @Query("Select * from WineEntity order by journalist_count desc limit 10")
    fun getTopNumberOfReviewers(): List<WineEntity>

    @Query("Select * from WineEntity order by confidence_index asc limit 10")
    fun getHighestConfidence(): List<WineEntity>

    @Query("Select * from WineEntity where is_primeurs = 1 order by confidence_index desc limit 10")
    fun getPrimeurs(): List<WineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWine(wineResponse: WineEntity)

    @Query("select * from WineEntity limit 1")
    fun isDatabasePopulated(): WineEntity?

    // Reviewer Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWineReview(wineReview: WineReviewsEntity)

    @Query("Select * from WineReviewsEntity order by title")
    fun getAllReviews(): List<WineReviewsEntity>

    @Query("select * from WineReviewsEntity order by points desc limit 10")
    fun getTopPoints(): List<WineReviewsEntity>

    @Query("select * from WineReviewsEntity order by price desc limit 20")
    fun getHighestPrice(): List<WineReviewsEntity>

    // ReviewType / Description
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReviewTypes(reviewTypeDescription: ReviewTypeEntity)

    @Query("select type_description from ReviewTypeEntity Limit 100")
    fun getReviewTypes(): List<String>

}