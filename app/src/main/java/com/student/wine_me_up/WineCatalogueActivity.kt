package com.student.wine_me_up

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.student.wine_me_up.models.SourceOfData
import com.student.wine_me_up.utilities.BaseMethods
import com.student.wine_me_up.utilities.GlobalWineDisplayAdapter
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_wine_catalogue.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WineCatalogueActivity : AppCompatActivity() {

    private lateinit var sourceOfData: SourceOfData

    private lateinit var displayList: ListView

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_wine_catalogue)

        sourceOfData = intent.extras?.get("dataSource") as SourceOfData

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wine_catalogue)

        CoroutineScope(Dispatchers.IO).launch {
            displayList = wineListView
            val wineCatalogue =
                BaseMethods.convertToWineModelSet(WineDatabase.getInstance(applicationContext).wineDao().getAllWines().toSet())
            val adapter = GlobalWineDisplayAdapter(
                applicationContext,
                wineCatalogue.toList()
            )
            runOnUiThread { displayList.adapter = adapter }
        }
        setListeners()
    }

    private fun retrieveData(sourceOfData: SourceOfData): Set<Any?> {
        if (sourceOfData == SourceOfData.GLOBAL_API) {
            return BaseMethods.convertToWineModelSet(WineDatabase.getInstance(applicationContext).wineDao().getAllWines().toSet())
        }
        return BaseMethods.convertToWineReviewSet(WineDatabase.getInstance(applicationContext).wineDao().getAllReviews().toSet())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            finish()
        }

        barcodeFloatingButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.barcode_scanner_coming_soon), Toast.LENGTH_LONG)
                .show()
        }
    }
}