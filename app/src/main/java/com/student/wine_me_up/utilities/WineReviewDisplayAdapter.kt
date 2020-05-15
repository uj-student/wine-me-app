package com.student.wine_me_up.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.student.wine_me_up.R
import com.student.wine_me_up.models.WineReviewsModel

class WineReviewDisplayAdapter(context: Context, private val data: List<WineReviewsModel>): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.run { inflate(R.layout.list_item_wine, parent, false) }

        val titleTextView = rowView.findViewById(R.id.wineName) as TextView

        val subtitleTextView = rowView.findViewById(R.id.wineSubtitle) as TextView

        val detailTextView = rowView.findViewById(R.id.wineDetails) as TextView

        val wineTypeTextView = rowView.findViewById(R.id.wineType) as TextView

        val wine = getItem(position) as WineReviewsModel

        titleTextView.text = wine.title
        subtitleTextView.text = wine.description
        wineTypeTextView.text = wine.designation

//        detailTextView.text = "More"

        return rowView    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}