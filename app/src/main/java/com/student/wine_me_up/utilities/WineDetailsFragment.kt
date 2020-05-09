package com.student.wine_me_up.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.student.wine_me_up.R
import com.student.wine_me_up.models.WineEntries
import kotlinx.android.synthetic.main.fragment_wine_details.*
import kotlinx.android.synthetic.main.list_item_wine.view.*
import kotlin.reflect.typeOf


class WineDetailsFragment(private val wineObject: WineEntries) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wine_details, container, false)
        populateCard(view)
        return view
    }

    private fun populateCard(view: View) {
        val wineText: TextView = view.findViewById(R.id.wineName)
        val appellation: TextView = view.findViewById(R.id.appellation)
        val wineColour: TextView = view.findViewById(R.id.wineColour)
        val wineType: TextView = view.findViewById(R.id.wineType)
        val wineRegions: TextView = view.findViewById(R.id.wineRegions)
        val wineCountry: TextView = view.findViewById(R.id.wineCountry)
        val wineClassification: TextView = view.findViewById(R.id.wineClassification)
        val wineVintage: TextView = view.findViewById(R.id.wineVintage)
        val wineDate: TextView = view.findViewById(R.id.wineDate)
        val isPrimeurs: TextView = view.findViewById(R.id.isPremeurs)
        val wineScore: TextView = view.findViewById(R.id.wineScore)
        val wineConfidence: TextView = view.findViewById(R.id.wineConfidence)
        val journalistCount: TextView = view.findViewById(R.id.journalistCount)


        wineText.text = wineObject.wine
        appellation.text = cardDetails(getString(R.string.appellation_label), wineObject.appellation)
        wineColour.text = cardDetails(getString(R.string.color_label), wineObject.color)
        wineType.text = cardDetails(getString(R.string.type_label),wineObject.wine_type)
        wineRegions.text = cardDetails(getString(R.string.region_label), wineObject.regions[0])
        wineCountry.text = cardDetails(getString(R.string.country_label), wineObject.country)
        wineClassification.text = cardDetails(getString(R.string.classification_label), wineObject.classification)
        wineVintage.text = cardDetails(getString(R.string.vintage_label), wineObject.vintage)
        wineDate.text = cardDetails(getString(R.string.date_label), wineObject.date)
        isPrimeurs.text = cardDetails(getString(R.string.primeurs_label), wineObject.is_primeurs.toString())
        wineScore.text = cardDetails(getString(R.string.score_label), wineObject.score.toString())
        wineConfidence.text = cardDetails(getString(R.string.confidence_label), wineObject.confidence_index)
        journalistCount.text = cardDetails(getString(R.string.reviewer_count_label), wineObject.journalist_count.toString())

    }

    private fun cardDetails(label: String, details: String?): String?{
        if (details.isNullOrBlank()) {
            return String.format(label, getString(R.string.not_available))
        }
        return String.format(getString(R.string.appellation_label), details)

    }
}

