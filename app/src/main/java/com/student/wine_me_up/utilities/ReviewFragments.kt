package com.student.wine_me_up.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.student.wine_me_up.R
import com.student.wine_me_up.models.WineReviewsModel
import kotlin.math.roundToInt

class ReviewFragments(private val reviewObject: WineReviewsModel) : Fragment() {

    private var review: WineReviewsModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wine_reviews, container, false)

        arguments?.let {
            review = it.getSerializable("review") as WineReviewsModel?
            review?.let {
                populateCard(view)
            }
        }
        populateCard(view)
        return view
    }

    companion object {
        fun newInstance(reviewObject: WineReviewsModel) = ReviewFragments(reviewObject).apply {
            arguments = Bundle().apply {
                putSerializable("review", reviewObject)
            }
        }
    }

    private fun populateCard(view: View) {
        val wineText: TextView = view.findViewById(R.id.wineTitle)
        val points: TextView = view.findViewById(R.id.points)
        val description: TextView = view.findViewById(R.id.description)
        val tasterName: TextView = view.findViewById(R.id.tasterName)
        val tasterTwitterHandle: TextView = view.findViewById(R.id.tasterTwitterHandle)
        val price: TextView = view.findViewById(R.id.Price)
        val designation: TextView = view.findViewById(R.id.designation)
        val variety: TextView = view.findViewById(R.id.variety)
        val region1: TextView = view.findViewById(R.id.region1)
        val origin: TextView = view.findViewById(R.id.origin)
        val winery: TextView = view.findViewById(R.id.winery)

        wineText.text = reviewObject.title
        points.text = cardDetails(getString(R.string.points), reviewObject.points)
        description.text = cardDetails(getString(R.string.description), reviewObject.description)
        tasterName.text = cardDetails(getString(R.string.taster_name), reviewObject.taster_name)
        tasterTwitterHandle.text = cardDetails(
            getString(R.string.taster_twitter_handle),
            reviewObject.taster_twitter_handle
        )
        price.text =
            cardDetails(getString(R.string.price), getWinePrice(reviewObject.price).toString())
        designation.text = cardDetails(getString(R.string.designation), reviewObject.designation)
        variety.text = cardDetails(getString(R.string.variety), reviewObject.variety)
        region1.text = cardDetails(getString(R.string.region_1), reviewObject.region_1)
        origin.text = cardDetails(
            getString(R.string.origin),
            "${reviewObject.province}, ${reviewObject.country}"
        )
        winery.text = cardDetails(getString(R.string.winery), reviewObject.winery)
    }

    private fun cardDetails(label: String, details: String?): String? {
        if (details.isNullOrBlank() || details == "0") {
            return String.format(label, getString(R.string.not_available))
        }
        return String.format(label, details)

    }

    private fun getWinePrice(usdPrice: Int?): Int {
        if (usdPrice != null) {
            return (usdPrice * 18.43).roundToInt()
        }
        return 0
    }
}