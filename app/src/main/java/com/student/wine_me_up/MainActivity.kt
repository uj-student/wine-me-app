package com.student.wine_me_up

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.student.wine_me_up.network.ApiController
import com.student.wine_me_up.wine_recommendation.RecommendationsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_bar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_burger_icon)


//        getWines()
        ApiController._isNetworkDone.observe(this, Observer {
            it?.let {
                if (it){
                    Log.d("DIALOG: ", it.toString())
                    if (dialog.isShowing){
                        dialog.dismiss()
                    }
                }
            }
        })

        setListeners()
        iconInfo()
    }

    private fun setDialog(show: Boolean) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this@MainActivity)
        val vl = inflater.inflate(R.layout.progress_bar, null)
//        val message = vl.findViewById(R.id.loadingMsg)
//        message.setText(display_message)
        builder.setView(vl)
        dialog = builder.create()
        if (show) {
            dialog.show()
        } else {
            dialog.cancel()
        }
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

        refreshFloatingButton.setOnClickListener {
            getWines()
            setDialog(true)
        }
    }

    private fun getWines() {
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