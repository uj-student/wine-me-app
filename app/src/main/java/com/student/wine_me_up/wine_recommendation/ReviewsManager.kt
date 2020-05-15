package com.student.wine_me_up.wine_recommendation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.wine_repo.ReviewTypeEntity
import com.student.wine_me_up.wine_repo.WineDatabase
import java.io.IOException


class ReviewsManager {

    private lateinit var wineReviews: List<WineReviewsModel>

    companion object {
        val isJsonDone = MutableLiveData<Boolean>()
        val _isJsonDone: LiveData<Boolean>
            get() = isJsonDone
    }


    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun convertToWineReviewsModels(data: String?): List<WineReviewsModel>? {
        val reviews = object : TypeToken<List<WineReviewsModel>>() {}.type
        return Gson().fromJson(data, reviews)

    }

    fun getDistinctReviews(reviews: List<WineReviewsModel>?): Set<ReviewTypeEntity> {
        val reviewsOptions = mutableSetOf<ReviewTypeEntity>()

        if (reviews != null) {
            for (review in reviews) {
                if (!review.region_1.isNullOrEmpty()) {
                    reviewsOptions.add(ReviewTypeEntity(null, review.region_1.capitalize()))
                }
                if (!review.country.isNullOrEmpty()) {
                    reviewsOptions.add(ReviewTypeEntity(null, review.country.capitalize()))
                }
                if (!review.winery.isNullOrEmpty()) {
                    reviewsOptions.add(ReviewTypeEntity(null, review.winery.capitalize()))
                }
                if (!review.variety.isNullOrEmpty()) {
                    reviewsOptions.add(ReviewTypeEntity(null, review.variety.capitalize()))
                }
                if (!review.description.isNullOrEmpty()) {
                    val desc = review.description.split(" ")
                    for (i in desc) {
                        val entry = i.trim().replace(".", "").replace(",", "")
                        if (!entry.contains("'")) {
                            if (entry.length > 2) {
                                reviewsOptions.add(ReviewTypeEntity(null, entry.capitalize()))
                            }
                        }
                    }
                }
            }
        }
        return reviewsOptions
    }

    fun saveReviewTypesToDb(context: Context, reviewType: Set<ReviewTypeEntity>) {
        for (review in reviewType) {
            WineDatabase.getInstance(context).wineDao().saveReviewTypes(review)
        }
    }
}