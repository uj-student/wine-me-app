package com.student.wine_me_up

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.student.wine_me_up.models.SourceOfData
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.network.ApiController
import com.student.wine_me_up.utilities.BaseMethods
import com.student.wine_me_up.wine_recommendation.RecommendationsActivity
import com.student.wine_me_up.wine_recommendation.ReviewsManager
import com.student.wine_me_up.wine_recommendation.ReviewsManager.Companion.isJsonDone
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var sharedPreferences: SharedPreferences
    private val networkCallTAG = "networkCallDone"
    private val jsonReviewTAG = "jsonSaveDone"

    private lateinit var wineReviews: List<WineReviewsModel>
    private var dataSource = SourceOfData.GLOBAL_API

    private lateinit var dialog: Dialog
    private val reviewsManager = ReviewsManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_burger_icon)
        sharedPreferences = getSharedPreferences(networkCallTAG, Context.MODE_PRIVATE)

        if (!sharedPreferences.contains("network")) {
            getWines()
            val editor = sharedPreferences.edit()
            editor.putString("network", networkCallTAG)
            editor.apply()
        }

        if (!sharedPreferences.contains("json")) {
            loadReviews()
            val editor = sharedPreferences.edit()
            editor.putString("json", jsonReviewTAG)
            editor.apply()
        }

        setListeners()
        iconInfo()
    }

    private fun loadReviews() {
        ReviewsManager._isJsonDone.observe(this, Observer {
            it?.let {
                if (it) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
            }
        })
        setDialog(true)
        val output = reviewsManager.getJsonDataFromAsset(
            applicationContext,
            fileName = "wine_reviewers.json"
        )
        val reviewList = CoroutineScope(Dispatchers.Default).async {
            withContext(Dispatchers.Main) {
                isJsonDone.postValue(false)
                reviewsManager.convertToWineReviewsModels(output)
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                saveReviewsToDb(reviewList.await())
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val reviews = reviewsManager.getDistinctReviews(reviewList.await())
                ReviewsManager().saveReviewTypesToDb(this@MainActivity, reviews)
                isJsonDone.postValue(true)
            }
        }
    }

    private fun saveReviewsToDb(jsonReviews: List<WineReviewsModel>?) {
        jsonReviews?.let {
            for (review in 0..500) { // grab the first 500
                WineDatabase.getInstance(applicationContext).wineDao()
                    .saveWineReview(
                        BaseMethods.convertReviewModelsToEntities(jsonReviews[review])
                    )
            }
        }
    }


    private fun setDialog(show: Boolean) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@MainActivity)
        val vl = inflater.inflate(R.layout.progress_bar, null)
//        val message = vl.findViewById(R.id.loadinMsg)
//        message.setText("Hello")
        builder.setView(vl)
        dialog = builder.create()
        if (show) {
            dialog.show()
        } else {
            dialog.cancel()
        }
    }

    private fun setListeners() {

        globalScore.setOnClickListener {
            dataSource = SourceOfData.GLOBAL_API
        }

        twitterReviewers.setOnClickListener {
            dataSource = SourceOfData.JSON_FILE
        }

        wineCatalogue.setOnClickListener {
            val intent = Intent(applicationContext, WineCatalogueActivity::class.java)
            intent.putExtra("dataSource", dataSource)
            startActivity(intent)
        }

        wineRatings.setOnClickListener {
            val intent = Intent(this, WineRatingActivity::class.java)
            intent.putExtra("dataSource", dataSource)
            startActivity(intent)
        }

        trySomethingNew.setOnClickListener {
            val intent = Intent(applicationContext, RecommendationsActivity::class.java)
            intent.putExtra("dataSource", dataSource)
            startActivity(intent)
        }

        wineScan.setOnClickListener {
            Toast.makeText(this, getString(R.string.barcode_scanner_coming_soon), Toast.LENGTH_LONG)
                .show()
        }

        refreshFloatingButton.setOnClickListener {
            getWines()
        }
    }

    private fun getWines() {
        ApiController._isNetworkDone.observe(this, Observer {
            it?.let {
                if (it) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
            }
        })
        setDialog(true)
        ApiController().makeCall(applicationContext)
    }

    private fun iconInfo() {
        wineCatalogue.setOnLongClickListener {
            Toast.makeText(this, getString(R.string.wine_catalogue), Toast.LENGTH_LONG).show()
            true
        }

        wineRatings.setOnLongClickListener {
            Toast.makeText(this, getString(R.string.wine_ratings), Toast.LENGTH_LONG).show()
            true
        }

        trySomethingNew.setOnLongClickListener {
            Toast.makeText(this, getString(R.string.get_wine_recommendations), Toast.LENGTH_LONG)
                .show()
            true
        }

        wineScan.setOnLongClickListener {
            Toast.makeText(this, getString(R.string.scan_wine_barcode), Toast.LENGTH_LONG).show()
            true
        }

        refreshFloatingButton.setOnLongClickListener {
            Toast.makeText(this, getString(R.string.refresh_wine_list), Toast.LENGTH_LONG).show()
            true
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}