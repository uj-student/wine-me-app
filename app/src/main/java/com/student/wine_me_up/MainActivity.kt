package com.student.wine_me_up

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.student.wine_me_up.network.ApiController
import com.student.wine_me_up.wine_recommendation.RecommendationsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_burger_icon)

//        getWines()


        setListeners()
        cardInfo()
    }

    private fun setListeners() {
        wineCatalogue.setOnClickListener {
            val intent = Intent(applicationContext, WineCatalogueActivity::class.java)
            startActivity(intent)
        }

        wineRatings.setOnClickListener {
            val intent = Intent(this, WineRatingActivity::class.java)
            startActivity(intent)
        }

        trySomethingNew.setOnClickListener {
            val intent = Intent(applicationContext, RecommendationsActivity::class.java)
            startActivity(intent)
        }

        wineScan.setOnClickListener {
            Toast.makeText(this, getString(R.string.barcode_scanner_coming_soon), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getWines() {
        ApiController().makeCall(applicationContext)
    }

    private fun cardInfo() {
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
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


//    private fun showPopUp() {
//        val intent = Intent(this, PopUpWindow::class.java)
//        intent.putExtra("popuptitle", "Coming Soon")
//        intent.putExtra("popuptext", "So sorry, service currently unavailable.")
//        intent.putExtra("popupbtn", "OK")
//        intent.putExtra("darkstatusbar", false)
//        startActivity(intent)
//
//    }
}