package com.student.wine_me_up.wine_recommendation

import com.student.wine_me_up.R
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isNotEmpty
import com.google.android.material.chip.Chip
import com.student.wine_me_up.models.WineEntries
import com.student.wine_me_up.utilities.BaseMethods.convertToWineEntries
import com.student.wine_me_up.utilities.WineDisplayAdapter
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_recommendation.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RecommendationsActivity : AppCompatActivity() {

    //    val preferences = arrayOf("wine_type", "color", "classification", "appellation")
    private lateinit var slideUpAnimation: Animation
    private lateinit var slideDownAnimation: Animation

    private lateinit var wineCategory: Set<WineEntries>
    private lateinit var winePreferences: Set<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recommendation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wine_types)

        clWineRecommendations.visibility = View.GONE

        slideUpAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        slideDownAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)


//        if (recommendationActivity != null) {
//            if (savedInstanceState != null) {
//                return
//            }
//        }

        GlobalScope.launch {
            wineCategory =
                convertToWineEntries(WineDatabase.getInstance(applicationContext).wineDao().getAllWines().toSet())
            val wineSet = getListOfCheckBoxes(wineCategory.toList())
            runOnUiThread {
                winePreferences = populateCheckBoxes(wineSet)
            }
        }

        setListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getListOfCheckBoxes(wines: List<WineEntries>?): Set<String> {
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
            wineCategoryChipGroup.clearCheck()
        }

        nextButton.setOnClickListener {
            if (!winePreferences.isNullOrEmpty()) {
                slideUpToRecommendationsAnimation()
                clWineRecommendations.visibility = View.VISIBLE
                supportActionBar?.title = getString(R.string.wine_recommendations)

                GlobalScope.launch {
                    val recommendations =
                        WineRecommendFactory(wineCategory.toList()).contentBasedFiltering(
                            winePreferences
                        )
                    val adapter = WineDisplayAdapter(applicationContext, recommendations)
                    runOnUiThread {
                        lvWineRecommendations.adapter = adapter
                    }
                }

            } else {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
            }
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
                val msg =
                    "You have " + (if (isChecked) "checked" else "unchecked") + " ${chip.text}."
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            wineCategoryChipGroup.addView(chip)
        }
        return preferences
    }

    private fun addFragment() {
        val recommendation = RecommendationFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.wineCategoryChipGroup, recommendation, null)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}