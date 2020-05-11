package com.student.wine_me_up

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.student.wine_me_up.models.SourceOfData
import com.student.wine_me_up.models.WineReviewsModel
import com.student.wine_me_up.utilities.BaseMethods
import com.student.wine_me_up.utilities.GlobalWineDisplayAdapter
import com.student.wine_me_up.utilities.WineDetailsFragment
import com.student.wine_me_up.utilities.WineReviewDisplayAdapter
import com.student.wine_me_up.wine_repo.WineDao
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_wine_rating.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.student.wine_me_up.models.WineModel as WineEntries1

class WineRatingActivity : AppCompatActivity() {

    private lateinit var displayList: ListView
    private lateinit var scoreChip: Chip
    private lateinit var confidenceChip: Chip
    private lateinit var reviewerChip: Chip
    private lateinit var primeursChip: Chip

    private lateinit var sortByScore: Set<WineEntries1>
    private lateinit var sortByConfidence: Set<WineEntries1>
    private lateinit var sortByReviewers: Set<WineEntries1>
    private lateinit var sortByPrimeurs: Set<WineEntries1>

    private lateinit var sortByPrice: Set<WineReviewsModel>
    private lateinit var sortByPoints: Set<WineReviewsModel>

    private lateinit var sourceOfData: SourceOfData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wine_rating)

        sourceOfData = intent.extras?.get("dataSource") as SourceOfData

        supportActionBar?.title = getString(R.string.wine_ratings)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayList = lvSortedWineList
        scoreChip = score_chip
        reviewerChip = reviewers_chip
        confidenceChip = confidence_chip
        primeursChip = primeurs_chip

        setListeners()

        if (sourceOfData == SourceOfData.GLOBAL_API) {
            CoroutineScope(Dispatchers.IO).launch {
                getGlobalWineLists()
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                getReviewList()
            }
            confidenceChip.visibility = View.GONE
            reviewerChip.visibility = View.GONE

            scoreChip.text = "Points"
            primeursChip.text = "Price"
        }
    }

    private fun getGlobalWineLists() {
        sortByScore =
            BaseMethods.convertToWineModelSet(accessDB().getTopScoreWine().toSet())
        sortByConfidence =
            BaseMethods.convertToWineModelSet(accessDB().getHighestConfidence().toSet())
        sortByReviewers =
            BaseMethods.convertToWineModelSet(accessDB().getTopNumberOfReviewers().toSet())
        sortByPrimeurs =
            BaseMethods.convertToWineModelSet(accessDB().getPrimeurs().toSet())
    }

    private fun accessDB(): WineDao {
        return WineDatabase.getInstance(this).wineDao()
    }

    private fun getReviewList() {
        sortByPrice = BaseMethods.convertToWineReviewSet(accessDB().getHighestPrice().toSet())
        sortByPoints = BaseMethods.convertToWineReviewSet(accessDB().getTopPoints().toSet())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkIfResultsFound(listOfWineResults: List<*>) {
        if (listOfWineResults.isNullOrEmpty()) {
            Toast.makeText(this, "No available wines", Toast.LENGTH_SHORT).show()
            return
        }
        populateViews(listOfWineResults)
        Toast.makeText(this, "Found some wines", Toast.LENGTH_SHORT).show()

    }

    private fun setListeners() {
        if (sourceOfData == SourceOfData.GLOBAL_API) {
            confidenceChip.setOnClickListener {
                checkIfResultsFound(sortByConfidence.toList())
            }

            scoreChip.setOnClickListener {
                checkIfResultsFound(sortByScore.toList())
            }

            reviewerChip.setOnClickListener {
                checkIfResultsFound(sortByReviewers.toList())
            }

            primeursChip.setOnClickListener {
                checkIfResultsFound(sortByPrimeurs.toList())
            }
        } else {
            scoreChip.setOnClickListener {
                checkIfResultsFound(sortByPoints.toList())
            }
            primeursChip.setOnClickListener {
                checkIfResultsFound(sortByPrice.toList())
            }
        }

        displayList.setOnItemClickListener { parent, view, position, id ->
            var wineList = sortByScore
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (confidenceChip.isSelected)
                wineList = sortByConfidence
            else if (reviewerChip.isSelected)
                wineList = sortByReviewers

            val wineDetails = WineDetailsFragment(wineList.toList()[position])
            fragmentTransaction.add(R.id.clWineRating, wineDetails, null)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun populateViews(sortedWines: List<*>) {
        if (sourceOfData == SourceOfData.GLOBAL_API) {
            val adapter = GlobalWineDisplayAdapter(this, sortedWines as List<WineEntries1>)

            displayList.adapter = adapter
        } else {
            val adapter = WineReviewDisplayAdapter(this, sortedWines as List<WineReviewsModel>)

            displayList.adapter = adapter
        }


    }

    fun ratingType(wines: List<WineEntries1>?, sortBy: String): Set<String> {
        val wineRatingsSort = mutableSetOf<String>()

        when (sortBy) {
            "Score" -> wines?.sortedBy { it -> it.score }
            "Confidence" -> wines?.sortedBy { it -> it.confidence_index }
            "Number of reviewers" -> wines?.sortedBy { it -> it.journalist_count }
        }
        // check if wines is sorted or needs to be assigned to variable

        for (wine in 0..9) {
            val listV = ListView(this)
            wines?.get(wine)
        }

        wines?.let {
            for (wine in wines) {
                when (sortBy) {
                    "Score" -> wineRatingsSort.add(wine.score.toString())
                    "Confidence" -> wineRatingsSort.add(wine.confidence_index)
                    "Number of reviewers" -> wineRatingsSort.add(wine.journalist_count.toString())
                }
            }
        }
        return wineRatingsSort
    }

    fun listWinesBasedOnRatings(wines: List<WineEntries1>?, ratingList: Set<String>) {
        val displayList = listOf<WineEntries1>()

        val sortedWines: List<WineEntries1>?

        sortedWines = wines?.sortedBy { it ->
            it.score
        }
    }
}
