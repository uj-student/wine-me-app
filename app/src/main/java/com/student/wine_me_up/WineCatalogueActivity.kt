package com.student.wine_me_up

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.student.wine_me_up.utilities.BaseMethods
import com.student.wine_me_up.utilities.WineDisplayAdapter
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.android.synthetic.main.activity_wine_catalogue.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WineCatalogueActivity : AppCompatActivity() {

    private lateinit var displayList: ListView

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_wine_catalogue)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wine_catalogue)

//        val wineCatalogue = WineDatabase.getInstance(this).wineDao().getAllWines()
        GlobalScope.launch {
            displayList = wineListView
            val wineCatalogue =
                BaseMethods.convertToWineEntries(WineDatabase.getInstance(applicationContext).wineDao().getAllWines().toSet())
            val adapter = WineDisplayAdapter(
                applicationContext,
                wineCatalogue.toList()
            )
            runOnUiThread { displayList.adapter = adapter }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}