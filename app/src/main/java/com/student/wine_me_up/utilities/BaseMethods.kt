package com.student.wine_me_up.utilities

import com.student.wine_me_up.models.WineEntries
import com.student.wine_me_up.wine_repo.Wine

object BaseMethods {
    fun convertToWineEntries(wineList: Set<Wine>): Set<WineEntries> {
        val temp = mutableSetOf<WineEntries>()
        for (wine in wineList) {
            temp.add(convertWinesToWineEntries(wine))
        }

        return temp
    }

    fun convertWineEntriesToWine(wine: WineEntries): Wine {
        return Wine(
            wine.wine_id,
            wine.wine,
            wine.wine_slug,
            wine.appellation,
            wine.appellation_slug,
            wine.color,
            wine.wine_type,
            wine.regions[0],
            wine.country,
            wine.classification,
            wine.vintage,
            wine.date,
            wine.is_primeurs,
            wine.score,
            wine.confidence_index,
            wine.journalist_count,
            wine.lwin,
            wine.lwin_11
        )
    }

    private fun convertWinesToWineEntries(wine: Wine): WineEntries {
        return WineEntries(
            wine.wine,
            wine.wine_id,
            wine.wine_slug,
            wine.appellation,
            wine.appellation_slug,
            wine.color,
            wine.wine_type,
            listOf(wine.regions),
            wine.country,
            wine.classification,
            wine.vintage,
            wine.date,
            wine.is_primeurs,
            wine.score,
            wine.confidence_index,
            wine.journalist_count,
            wine.lwin,
            wine.lwin_11
        )
    }
}