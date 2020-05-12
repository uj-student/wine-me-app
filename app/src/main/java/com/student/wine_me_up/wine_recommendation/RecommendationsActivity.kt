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
import com.student.wine_me_up.utilities.BaseMethods.convertToWineModelSet
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

    private lateinit var wineCategory: Set<WineModel>
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
                    getListOfCheckBoxesForGlobalScores(wineCategory.toList())
            } else {
                withContext(Dispatchers.IO) {
                    keyWords = WineDatabase.getInstance(applicationContext)
                        .wineDao().getReviewTypes().toSet()
                }
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
                    if (sourceOfData == SourceOfData.GLOBAL_API) {
                        wineRecommendationList =
                            WineRecommendFactory(wineCategory.toList()).contentBasedFiltering(
                                winePreferences
                            )
                        val adapter =
                            GlobalWineDisplayAdapter(applicationContext, wineRecommendationList)
                        runOnUiThread {
                            lvWineRecommendations.adapter = adapter
                        }
                    } else {
                        // TODO: pass review data to recommendation factory
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
}
