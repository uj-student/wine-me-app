package com.student.wine_me_up

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.student.wine_me_up.utilities.BaseMethods
import com.student.wine_me_up.utilities.WineDetailsFragment
import com.student.wine_me_up.utilities.WineDisplayAdapter
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_wine_rating.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.student.wine_me_up.models.WineEntries as WineEntries1

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wine_rating)

        supportActionBar?.title = getString(R.string.wine_ratings)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayList = lvSortedWineList
        scoreChip = score_chip
        reviewerChip = reviewers_chip
        confidenceChip = confidence_chip
        primeursChip = primeurs_chip


        setListeners()

        GlobalScope.launch {
            getLists()
        }

    }


    private fun getLists() {
        sortByScore =
            BaseMethods.convertToWineEntries(WineDatabase.getInstance(this).wineDao().getTopScoreWine().toSet())
        sortByConfidence =
            BaseMethods.convertToWineEntries(WineDatabase.getInstance(this).wineDao().getHighestConfidence().toSet())
        sortByReviewers =
            BaseMethods.convertToWineEntries(WineDatabase.getInstance(this).wineDao().getTopNumberOfReviewers().toSet())
        sortByPrimeurs =
            BaseMethods.convertToWineEntries(WineDatabase.getInstance(this).wineDao().getPrimeurs().toSet())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkIfResultsFound(listOfWineResults: List<WineEntries1>) {
        if (listOfWineResults.isNullOrEmpty()) {
            Toast.makeText(this, "No available wines", Toast.LENGTH_SHORT).show()
            return
        }
        populateViews(listOfWineResults)
        Toast.makeText(this, "Found some wines", Toast.LENGTH_SHORT).show()

    }

    private fun setListeners() {
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

    private fun populateViews(sortedWines: List<WineEntries1>) {
        val adapter = WineDisplayAdapter(this, sortedWines)
        displayList.adapter = adapter
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
