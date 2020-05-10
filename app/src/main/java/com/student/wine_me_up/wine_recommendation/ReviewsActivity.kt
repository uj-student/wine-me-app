package com.student.wine_me_up.wine_recommendation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.utilities.BaseMethods.convertReviewModelsToEntities
import com.student.wine_me_up.utilities.BaseMethods.convertToReviewModelSet
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class ReviewsActivity : AppCompatActivity() {

    private lateinit var wineReviews: List<WineReviewsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val output = getJsonDataFromAsset(applicationContext, fileName = "wine_reviewers.json")!!
        saveReviewsToDb(getDistinctWines(output))

    }


    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun getDistinctWines(data: String): List<WineReviewsModel>? {
        val reviews = object : TypeToken<List<WineReviewsModel>>() {}.type
        return Gson().fromJson(data, reviews)

    }

    private fun saveReviewsToDb(jsonReviews: List<WineReviewsModel>?) {
        GlobalScope.launch {
            jsonReviews?.let {
                for (review in it) {
                    WineDatabase.getInstance(applicationContext).wineDao()
                        .saveWineReview(
                            convertReviewModelsToEntities(review)
                        )
                }
            }
        }
    }

    fun retrieveReviews(): List<WineReviewsModel>? {
        GlobalScope.launch {
            wineReviews =
                convertToReviewModelSet(WineDatabase.getInstance(applicationContext).wineDao().getAllReviews())

            Log.d("REVIEWS: ", wineReviews[0].toString())
        }
        return wineReviews
    }
}