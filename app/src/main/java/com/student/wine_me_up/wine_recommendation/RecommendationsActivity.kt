package com.student.wine_me_up.wine_recommendation

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.student.wine_me_up.R
import com.student.wine_me_up.models.SourceOfData
import com.student.wine_me_up.models.WineModel
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.utilities.BaseMethods.convertToWineModelSet
import com.student.wine_me_up.utilities.BaseMethods.convertToWineReviewSet
import com.student.wine_me_up.utilities.GlobalWineDisplayAdapter
import com.student.wine_me_up.utilities.WineDetailsFragment
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_recommendation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecommendationsActivity : AppCompatActivity() {

    private lateinit var slideUpAnimation: Animation
    private lateinit var slideDownAnimation: Animation

    private lateinit var wineCategory: Set<*>
    private lateinit var winePreferences: Set<String>
    private lateinit var wineRecommendationList: List<WineModel>

    private lateinit var sourceOfData: SourceOfData
    private lateinit var keyWords: Set<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recommendation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wine_types)

        sourceOfData = intent.extras?.get("dataSource") as SourceOfData

        clWineRecommendations.visibility = View.GONE

        slideUpAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        slideDownAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)


        CoroutineScope(Dispatchers.IO).launch {
            if (sourceOfData == SourceOfData.GLOBAL_API) {
                withContext(Dispatchers.IO) {
                    val dbWines =
                        WineDatabase.getInstance(applicationContext).wineDao().getAllWines().toSet()
                    wineCategory =
                        convertToWineModelSet(dbWines)
                }
                keyWords =
                    getListOfCheckBoxesForGlobalScores(wineCategory.toList() as List<WineModel>)
            } else {
                withContext(Dispatchers.IO) {
                    val dbReviews = WineDatabase.getInstance(applicationContext)
                        .wineDao().getAllReviews().toSet()
                    wineCategory =
                        convertToWineReviewSet(dbReviews)
                }
                keyWords =
                    getListOfCheckBoxesForReviews(wineCategory.toList() as List<WineReviewsModel>)
            }
            runOnUiThread {
                winePreferences = populateCheckBoxes(keyWords)
            }
        }
        setListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getListOfCheckBoxesForGlobalScores(wines: List<WineModel>?): Set<String> {
        val wineCategoryList = mutableSetOf<String>()
        wines?.let {
            for (wine in it) {
                if (wine.wine_type.isNotEmpty()) {
                    wineCategoryList.add(wine.wine_type.capitalize())
                }
                if (wine.color.isNotEmpty()) {
                    wineCategoryList.add(wine.color.capitalize())
                }
                if (!wine.classification.isNullOrEmpty()) {
                    wineCategoryList.add(wine.classification!!.capitalize())
                }
                if (wine.appellation.isNotEmpty()) {
                    wineCategoryList.add(wine.appellation.capitalize())
                }
            }
        }
        return wineCategoryList
    }

    private fun getListOfCheckBoxesForReviews(reviews: List<WineReviewsModel>): Set<String> {
        val reviewsOptions = mutableSetOf<String>()

        for (review in reviews) {
            if (!review.region_1.isNullOrEmpty()) {
                reviewsOptions.add(review.region_1.capitalize())
            }
            if (!review.region_2.isNullOrEmpty()) {
                reviewsOptions.add(review.region_2.capitalize())
            }
            if (!review.country.isNullOrEmpty()) {
                reviewsOptions.add(review.country.capitalize())
            }
            if (!review.winery.isNullOrEmpty()) {
                reviewsOptions.add(review.winery.capitalize())
            }
            if (!review.variety.isNullOrEmpty()) {
                reviewsOptions.add(review.variety.capitalize())
            }
//            if (!review.description.isNullOrEmpty()){
//                val desc = review.description.split(" ")
//                for (i in desc) {
//                    reviewsOptions.add(i)
//                }
//            }
        }
        return reviewsOptions
    }

    private fun setListener() {

        removeAllButton.setOnClickListener {
            if (!winePreferences.isNullOrEmpty()) {
                wineCategoryChipGroup.clearCheck()
            } else {
                finish()
            }
        }

        nextButton.setOnClickListener {
            if (!winePreferences.isNullOrEmpty()) {
                slideUpToRecommendationsAnimation()
                clWineRecommendations.visibility = View.VISIBLE
                supportActionBar?.title = getString(R.string.wine_recommendations)

                CoroutineScope(Dispatchers.IO).launch {
                    wineRecommendationList =
                        WineRecommendFactory(wineCategory.toList() as List<WineModel>).contentBasedFiltering(
                            winePreferences
                        )
                    val adapter =
                        GlobalWineDisplayAdapter(applicationContext, wineRecommendationList)
                    runOnUiThread {
                        lvWineRecommendations.adapter = adapter
                    }
                }

            } else {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
            }
        }


        lvWineRecommendations.setOnItemClickListener { parent, view, position, id ->

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val wineDetails = WineDetailsFragment(wineRecommendationList[position])
            fragmentTransaction.replace(R.id.lvWineRecommendations, wineDetails, null)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        backButton.setOnClickListener {
            // slideDownBack()
            clWineRecommendations.visibility = View.GONE
            clWineTypes.visibility = View.VISIBLE
            supportActionBar?.title = getString(R.string.wine_types)
        }
    }

    private fun slideUpToRecommendationsAnimation() {
        slideUpAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                clWineTypes.clearAnimation()
                clWineTypes.visibility = View.GONE
            }
        })
        clWineTypes.startAnimation(slideUpAnimation)
    }

    private fun slideDownBack() {
        slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                clWineRecommendations.clearAnimation()
                clWineRecommendations.visibility = View.GONE
            }
        })
        clWineRecommendations.startAnimation(slideDownAnimation)
    }

    private fun populateCheckBoxes(wineCategoryList: Set<String>): Set<String> {
        val preferences = mutableSetOf<String>()
        for (wine in wineCategoryList) {
            val chip = Chip(this)
            chip.text = wine
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    preferences.add(chip.text.toString())
                } else {
                    preferences.remove(chip.text.toString())
                }
                if (!preferences.isNullOrEmpty()) {
                    removeAllButton.text = getString(R.string.remove_all)
                } else {
                    removeAllButton.text = getString(R.string.back_button)
                }

                val msg =
                    "You have " + (if (isChecked) "checked" else "unchecked") + " ${chip.text}."
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            wineCategoryChipGroup.addView(chip)
        }
        return preferences
    }


//    private fun addFragment() {
//        val recommendation = WineDetailsFragment()
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.wineCategoryChipGroup, recommendation, null)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }
}