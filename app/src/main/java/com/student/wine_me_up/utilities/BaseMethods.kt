package com.student.wine_me_up.utilities

import com.student.wine_me_up.models.WineModel
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.wine_repo.WineEntity
import com.student.wine_me_up.wine_repo.WineReviewsEntity

object BaseMethods {
    fun convertToWineModelSet(wineList: Set<WineEntity>): Set<WineModel> {
        val temp = mutableSetOf<WineModel>()
        for (wine in wineList) {
            temp.add(convertWinesToWineEntries(wine))
        }

        return temp
    }

    fun convertToWineReviewSet(reviewList: Set<WineReviewsEntity>): Set<WineReviewsModel> {
        val temp = mutableSetOf<WineReviewsModel>()
        for (review in reviewList) {
            temp.add(convertReviewEntriesToModels(review))
        }

        return temp
    }

    fun convertWineEntriesToWine(wine: WineModel): WineEntity {
        return WineEntity(
            wine.wine_id,
            wine.wine,
            wine.wine_slug,
            wine.appellation,
            wine.appellation_slug,
            wine.color,
            wine.wine_type,
            wine.regions[0],
            wine.country,
            wine.classification,
            wine.vintage,
            wine.date,
            wine.is_primeurs,
            wine.score,
            wine.confidence_index,
            wine.journalist_count,
            wine.lwin,
            wine.lwin_11
        )
    }

    private fun convertWinesToWineEntries(wine: WineEntity): WineModel {
        return WineModel(
            wine.wine,
            wine.wine_id,
            wine.wine_slug,
            wine.appellation,
            wine.appellation_slug,
            wine.color,
            wine.wine_type,
            listOf(wine.regions),
            wine.country,
            wine.classification,
            wine.vintage,
            wine.date,
            wine.is_primeurs,
            wine.score,
            wine.confidence_index,
            wine.journalist_count,
            wine.lwin,
            wine.lwin_11
        )
    }

    fun convertToReviewModelSet(reviewList: List<WineReviewsEntity>): List<WineReviewsModel> {
        val temp = mutableListOf<WineReviewsModel>()
        for (review in reviewList) {
            temp.add(convertReviewEntriesToModels(review))
        }

        return temp
    }

    private fun convertReviewEntriesToModels(reviews: WineReviewsEntity): WineReviewsModel {
        return WineReviewsModel(
            reviews.points,
            reviews.title,
            reviews.description,
            reviews.taster_name,
            reviews.taster_twitter_handle,
            reviews.price,
            reviews.designation,
            reviews.variety,
            reviews.region_1,
            reviews.region_2,
            reviews.province,
            reviews.country,
            reviews.winery
        )
    }

    fun convertReviewModelsToEntities(reviews: WineReviewsModel): WineReviewsEntity {
        return WineReviewsEntity(
            null,
            reviews.points,
            reviews.title,
            reviews.description,
            reviews.taster_name,
            reviews.taster_twitter_handle,
            reviews.price,
            reviews.designation,
            reviews.variety,
            reviews.region_1,
            reviews.region_2,
            reviews.province,
            reviews.country,
            reviews.winery
        )
    }

    fun splitString(description: String): String {
        val arrSplit = description.split(" ")
        for (i in arrSplit) {
            println(i)
        }
        return arrSplit.toString()
    }

}